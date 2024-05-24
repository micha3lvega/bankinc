package co.com.novatec.bankinc.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.com.novatec.bankinc.entity.Transaction;
import co.com.novatec.bankinc.enums.ECardStates;
import co.com.novatec.bankinc.enums.ETransactionState;
import co.com.novatec.bankinc.exception.ExpiredTimeException;
import co.com.novatec.bankinc.exception.InsufficientBalanceException;
import co.com.novatec.bankinc.exception.InvalidCardTransactionException;
import co.com.novatec.bankinc.exception.InvalidStateCardException;
import co.com.novatec.bankinc.exception.InvalidStateTransactionException;
import co.com.novatec.bankinc.exception.NotFoundException;
import co.com.novatec.bankinc.repository.TransactionRepository;
import co.com.novatec.bankinc.request.AddBalanceRequest;
import co.com.novatec.bankinc.request.AnulationTransactionRequest;
import co.com.novatec.bankinc.request.PurchaseRequest;
import co.com.novatec.bankinc.util.Constants;

/**
 * Servicio para realizar transacciones y gestionar las transacciones existentes.
 */
@Service
public class TransactionServices {

	private CardServices cardServices;
	private TransactionRepository transactionRepository;

	/**
	 * Crea una instancia del servicio de transacciones.
	 *
	 * @param cardServices           Servicio de tarjetas utilizado para buscar tarjetas.
	 * @param transactionRepository Repositorio de transacciones utilizado para acceder a las transacciones.
	 */
	public TransactionServices(CardServices cardServices, TransactionRepository transactionRepository) {
		this.cardServices = cardServices;
		this.transactionRepository = transactionRepository;
	}

	/**
	 * Busca una transacción por su ID.
	 *
	 * @param id el ID de la transacción a buscar
	 * @return la transacción encontrada
	 * @throws NotFoundException si no se encuentra la transacción con el ID
	 *                           especificado
	 */
	public Transaction findById(UUID id) {

		return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaccion no encontrada"));

	}

	/**
	 * Realiza una compra utilizando una solicitud de compra.
	 *
	 * @param purchaseRequest la solicitud de compra
	 * @return la transacción generada para la compra
	 * @throws InvalidStateCardException    si la tarjeta está inactiva
	 * @throws InsufficientBalanceException si el saldo de la tarjeta es
	 *                                      insuficiente para la compra
	 */
	public Transaction purchase(PurchaseRequest purchaseRequest) {

		var transaction = new Transaction();

		// Buscar la tarjeta
		var card = cardServices.findCardByID(purchaseRequest.getCardId());

		// validar que la tarjeta este activa
		if (!card.getState().equals(ECardStates.ACTIVE)) {
			throw new InvalidStateCardException("Tarjeta inactiva");
		}

		// Validar el saldo
		var balance = new BigDecimal(card.getBalance());
		var purchase = BigDecimal.valueOf(purchaseRequest.getPrice());

		// Configurar la transacción
		transaction.setCard(card);
		transaction.setAmount(purchase.toEngineeringString());

		transaction.setCreateDate(LocalDateTime.now());

		if (balance.compareTo(purchase) < 0) {

			// Guardar la transaccion en estado rechazado
			transaction.setState(ETransactionState.DECLINED);
			transactionRepository.save(transaction);
			throw new InsufficientBalanceException("Saldo insuficiente para realizar la compra");
		}

		// Actualizar el balance de la tarjeta
		var newBalance = balance.subtract(purchase);
		card.setBalance(newBalance.toEngineeringString());
		cardServices.update(card);

		transaction.setState(ETransactionState.SUCCESS);

		// Guardar la transacción
		return transactionRepository.save(transaction);

	}

	public Transaction anulation(AnulationTransactionRequest anulationTransactionRequest) {

		// Buscar la transaccion
		var transaction = findById(anulationTransactionRequest.getTransactionId());

		// Validar que la transaccion pertenezca a la tarjeta
		if (!transaction.getCard().getCardNumber().equals(anulationTransactionRequest.getCardId())) {
			throw new InvalidCardTransactionException();
		}

		// Validar que la transaccion este en estado completada
		if (!transaction.getState().equals(ETransactionState.SUCCESS)) {
			throw new InvalidStateTransactionException();
		}

		// Obtener la diferencia entre la fecha actual y la fecha de la creacion de la
		// transaccion
		var duration = Duration.between(transaction.getCreateDate(), LocalDateTime.now());

		// Validar si ya paso el tiempo maximo para anular una transaccion
		if (duration.toHours() > Constants.MAX_HOURS_ANULATION_VALUE) {
			throw new ExpiredTimeException();
		}

		// Cambiar el estado de la transaccion a anulada
		transaction.setState(ETransactionState.VOIDED);

		// Cargar el valor a la tarjeta
		var addBalanceRequest = new AddBalanceRequest();
		addBalanceRequest.setBalance(transaction.getAmount());
		addBalanceRequest.setCardId(transaction.getCard().getCardNumber());
		var card = transaction.getCard();

		// Generar el nuevo balance
		var newBalance = new BigDecimal(card.getBalance()).add(new BigDecimal(transaction.getAmount()));
		card.setBalance(newBalance.toEngineeringString());

		// Actualizar la tarjeta
		transaction.setCard(card);

		// Actualizar la transaccion
		return transactionRepository.save(transaction);
	}

}
