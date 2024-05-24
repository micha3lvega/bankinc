package co.com.novatec.bankinc.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import co.com.novatec.bankinc.builder.CardBuilder;
import co.com.novatec.bankinc.entity.Card;
import co.com.novatec.bankinc.entity.Product;
import co.com.novatec.bankinc.enums.ECardStates;
import co.com.novatec.bankinc.exception.InvalidStateCardException;
import co.com.novatec.bankinc.exception.NotFoundException;
import co.com.novatec.bankinc.repository.CardRepository;
import co.com.novatec.bankinc.request.AddBalanceRequest;
import co.com.novatec.bankinc.request.CardActiveRequest;
import co.com.novatec.bankinc.response.GetBalanceResponse;
import co.com.novatec.bankinc.util.Constants;
import co.com.novatec.bankinc.util.Util;
import net.datafaker.Faker;

/**
 * Servicio para realizar operaciones relacionadas con tarjetas.
 */
@Service
public class CardServices {

	private Faker faker;
	private ClientServices clientServices;
	private ProductServices productServices;
	private CardRepository cardRepository;

	/**
	 * Crea una instancia del servicio de tarjetas.
	 *
	 * @param faker           Instancia de Faker para generar datos aleatorios.
	 * @param clientServices  Servicio de clientes para generar un nuevo cliente asociado a la tarjeta.
	 * @param productServices Servicio de productos para obtener información del producto asociado a la tarjeta.
	 * @param cardRepository  Repositorio de tarjetas.
	 */
	public CardServices(Faker faker, ClientServices clientServices, ProductServices productServices,
			CardRepository cardRepository) {
		this.faker = faker;
		this.clientServices = clientServices;
		this.productServices = productServices;
		this.cardRepository = cardRepository;
	}

	/**
	 * Genera una nueva tarjeta a partir de un identificador de producto.
	 *
	 * @param productID el ID del producto para el cual se generará la tarjeta
	 * @return la tarjeta generada
	 */
	public Card generateCard(Long productID) {

		var card = new CardBuilder();

		// Crear cliente
		card.client(clientServices.generateNewClient());

		// Obtener el producto
		var product = productServices.findProductById(productID);
		card.product(product);

		// Generar numero de tarjeta
		var cardNumber = generateCardNumber(product);
		card.cardNumber(cardNumber);

		// Generar fecha de expedicion añadiendo 3 años a la fecha actual
		card.expirationDate(generatExpirationDate(Constants.NUMBER_YEARS_ADD_EXPIRATION_DATE));

		// Setear balance inicial
		card.balance(Constants.DEFAULT_INIT_BALANCE);

		// Guardar una tarjea con estado activo
		return cardRepository.save(card.inactive().build());

	}

	/**
	 * Activa una tarjeta existente cambiando su estado a activo.
	 *
	 * @param request la solicitud de activación de la tarjeta que contiene el ID de
	 *                la tarjeta a activar
	 * @return la tarjeta activada
	 * @throws NotFoundException si no se encuentra la tarjeta con el ID
	 *                           especificado
	 */
	public Card enroll(CardActiveRequest request) {

		// Buscar la tarjeta
		var card = findCardByID(request.getCardId());

		card.setState(ECardStates.ACTIVE);

		// Guardar la tarjeta y retornarla
		return cardRepository.save(card);

	}

	/**
	 * Bloquea una tarjeta existente cambiando su estado a bloqueado.
	 *
	 * @param cardId el ID de la tarjeta a bloquear
	 * @return la tarjeta bloqueada
	 * @throws NotFoundException si no se encuentra la tarjeta con el ID
	 *                           especificado
	 */
	public Card blockCard(String cardId) {

		// Buscar la tarjeta
		var card = findCardByID(cardId);

		// Cambiar el estado a activo
		card.setState(ECardStates.BLOCKED);

		// Guardar la tarjeta y retornarla
		return cardRepository.save(card);

	}

	/**
	 * Recarga el saldo de una tarjeta.
	 *
	 * @param request solicitud de recarga de saldo que contiene el identificador de
	 *                la tarjeta y el nuevo saldo.
	 * @return la tarjeta actualizada con el nuevo saldo.
	 * @throws NotFoundException         si no se encuentra la tarjeta con el
	 *                                   identificador especificado.
	 * @throws InvalidStateCardException si la tarjeta no se encuentra activa.
	 */
	public Card reloadBalanceCard(AddBalanceRequest request) {

		// Buscar la tarjeta
		var card = findCardByID(request.getCardId());

		// Validar que la tarjeta este activa
		if ((card == null) || (card.getState() == null) || !card.getState().equals(ECardStates.ACTIVE)) {
			throw new InvalidStateCardException();
		}

		// Generar saldo
		var newBalance = new BigDecimal(request.getBalance());
		var currentBalance = new BigDecimal(card.getBalance());

		// Añadir saldo anterior
		newBalance = newBalance.add(currentBalance).setScale(3, RoundingMode.HALF_UP);
		card = card.balance(newBalance);

		// Actualizar balance
		return cardRepository.save(card);

	}

	/**
	 * Obtiene el saldo de una tarjeta específica.
	 *
	 * @param cardId Identificador de la tarjeta.
	 * @return Objeto de respuesta que contiene el saldo de la tarjeta.
	 * @throws NotFoundException Si no se encuentra la tarjeta con el ID
	 *                           especificado.
	 */
	public GetBalanceResponse getBalance(String cardId) {

		var card = findCardByID(cardId);

		return new GetBalanceResponse(card);
	}

	/**
	 * Genera un número de tarjeta basado en el producto dado.
	 *
	 * @param product el producto para el cual se generará el número de tarjeta
	 * @return el número de tarjeta generado
	 */
	private String generateCardNumber(Product product) {

		// Obtener el numero de caracteres que se deben generar para completar la
		// tarjeta
		var remainingCharateresLength = Constants.LENGTH_NUMBER_CARD - product.getPrefix().length();

		// Crear los numeros que hacen falta para completarla tarjeta
		return product.getPrefix() + Util.generateRandomStringNumber(remainingCharateresLength);

	}

	/**
	 * Genera una fecha de vencimiento para la tarjeta, añadiendo un número
	 * específico de años a la fecha actual.
	 *
	 * @param numberAddYears el número de años a agregar a la fecha actual
	 * @return la fecha de vencimiento generada
	 */
	private LocalDate generatExpirationDate(int numberAddYears) {

		// Crear una instancia de la fecha actual
		var calendar = Calendar.getInstance();

		// Sumar el numero de años recibidos a la fecha actual
		calendar.add(Calendar.YEAR, numberAddYears);

		// Establecer el rango de fechas
		var startRange = Calendar.getInstance();
		startRange.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1); // Fecha de inicio

		var endRange = Calendar.getInstance();
		endRange.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31); // Fecha de fin

		var startDate = startRange.getTime();
		var endDate = calendar.getTime();

		// Utilzar faker para generar una fecha al azar entre los rangos
		return Util.dateToLocalDateTime(faker.date().between(startDate, endDate));

	}

	/**
	 * Busca una tarjeta por su ID.
	 *
	 * @param cardId el ID de la tarjeta a buscar
	 * @return la tarjeta encontrada
	 * @throws NotFoundException si no se encuentra la tarjeta con el ID
	 *                           especificado
	 */
	public Card findCardByID(String cardId) {
		return cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException("No se encontro la tarjeta"));
	}

	/**
	 * Actualiza la información de una tarjeta en la base de datos.
	 *
	 * @param card La tarjeta a actualizar.
	 * @return La tarjeta actualizada.
	 * @throws NotFoundException Si no se encuentra la tarjeta en la base de datos.
	 */
	public Card update(Card card) {

		if ((card == null) || (card.getCardNumber() == null)) {
			throw new IllegalArgumentException("La tarjeta o su ID no pueden ser nulos");
		}

		findCardByID(card.getCardNumber());
		return cardRepository.save(card);

	}

}
