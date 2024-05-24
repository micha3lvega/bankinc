package co.com.novatec.bankinc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import co.com.novatec.bankinc.entity.Card;
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
import co.com.novatec.bankinc.request.AnulationTransactionRequest;
import co.com.novatec.bankinc.request.PurchaseRequest;
import co.com.novatec.bankinc.services.CardServices;
import co.com.novatec.bankinc.services.TransactionServices;
import co.com.novatec.bankinc.util.Constants;

@DataJpaTest
@TestPropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionServicesTest {

	private TransactionServices transactionServices;

	@Mock
	private CardServices cardServices;

	@Mock
	private TransactionRepository transactionRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		transactionServices = new TransactionServices(cardServices, transactionRepository);
	}

	@Test
	void testFindById_TransactionFound() {

		var transactionId = UUID.randomUUID();
		var expectedTransaction = new Transaction();
		when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(expectedTransaction));

		var actualTransaction = transactionServices.findById(transactionId);

		assertSame(expectedTransaction, actualTransaction);
		verify(transactionRepository).findById(transactionId);
	}

	@Test
	void testFindById_TransactionNotFound() {

		var transactionId = UUID.randomUUID();
		when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> transactionServices.findById(transactionId));
		verify(transactionRepository).findById(transactionId);
	}

	@Test
	void testPurchase_ValidCardSufficientBalance_SuccessfulTransaction() {

		var cardId = "123";
		var price = 100d;
		var card = new Card();
		card.setState(ECardStates.ACTIVE);
		card.setBalance("100");
		var purchaseRequest = new PurchaseRequest(cardId, price);
		when(cardServices.findCardByID(cardId)).thenReturn(card);

		var transaction = new Transaction();
		transaction.setCard(card);
		transaction.setAmount("100.0");
		transaction.setState(ETransactionState.SUCCESS);
		transaction.setCreateDate(LocalDateTime.now());

		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		var actualTransaction = transactionServices.purchase(purchaseRequest);

		assertNotNull(actualTransaction);
		assertEquals(card, actualTransaction.getCard());
		assertEquals(Double.toString(price), actualTransaction.getAmount());
		assertEquals(ETransactionState.SUCCESS, actualTransaction.getState());
		assertNotNull(actualTransaction.getCreateDate());

		verify(cardServices).findCardByID(cardId);

	}

	@Test
	void testPurchase_InactiveCard_InvalidStateCardException() {

		var cardId = "123";
		var price = 10.0;
		var card = new Card();
		card.setState(ECardStates.INACTIVE);
		var purchaseRequest = new PurchaseRequest(cardId, price);
		when(cardServices.findCardByID(cardId)).thenReturn(card);

		assertThrows(InvalidStateCardException.class, () -> transactionServices.purchase(purchaseRequest));
		verify(cardServices).findCardByID(cardId);
		verify(transactionRepository, never()).save(any());
	}

	@Test
	void testPurchase_InsufficientBalance_InsufficientBalanceException() {

		var cardId = "123";
		var price = 10.0;
		var card = new Card();
		card.setState(ECardStates.ACTIVE);
		card.setBalance("5");
		var purchaseRequest = new PurchaseRequest(cardId, price);
		when(cardServices.findCardByID(cardId)).thenReturn(card);

		assertThrows(InsufficientBalanceException.class, () -> transactionServices.purchase(purchaseRequest));
		verify(cardServices).findCardByID(cardId);

	}

	@Test
	void testAnulation_ValidTransaction_SuccessfulAnulation() {

		var cardId = "123";
		var card = new Card();
		card.setCardNumber(cardId);
		card.setBalance("100");

		var transaction = new Transaction();
		var transactionId = UUID.randomUUID();
		transaction.setAmount("100");
		transaction.setCard(card);
		transaction.setState(ETransactionState.SUCCESS);
		transaction.setCreateDate(LocalDateTime.now().minusHours(Constants.MAX_HOURS_ANULATION_VALUE - 1));

		var anulationRequest = new AnulationTransactionRequest();
		anulationRequest.setCardId(cardId);
		anulationRequest.setTransactionId(transactionId);
		when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		var actualTransaction = transactionServices.anulation(anulationRequest);

		assertNotNull(actualTransaction);
		assertEquals(ETransactionState.VOIDED, actualTransaction.getState());
		assertEquals(card, actualTransaction.getCard());
		verify(transactionRepository).findById(transactionId);
		verify(transactionRepository).save(actualTransaction);
	}

	@Test
	void testAnulation_InvalidCard_InvalidCardTransactionException() {

		var transactionId = UUID.randomUUID();
		var cardId = "123";
		var transaction = new Transaction();
		var card = new Card();
		card.setCardNumber("456");
		transaction.setCard(card);

		var anulationRequest = new AnulationTransactionRequest();
		anulationRequest.setCardId(cardId);
		anulationRequest.setTransactionId(transactionId);
		when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

		assertThrows(InvalidCardTransactionException.class, () -> transactionServices.anulation(anulationRequest));
		verify(transactionRepository).findById(transactionId);
		verify(transactionRepository, never()).save(any());
	}

	@Test
	void testAnulation_InvalidTransactionState_InvalidStateTransactionException() {

		var transactionId = UUID.randomUUID();
		var cardId = "123";
		var transaction = new Transaction();
		var card = new Card();
		card.setCardNumber(cardId);
		transaction.setCard(card);
		transaction.setState(ETransactionState.DECLINED);

		var anulationRequest = new AnulationTransactionRequest();
		anulationRequest.setCardId(cardId);
		anulationRequest.setTransactionId(transactionId);
		when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

		assertThrows(InvalidStateTransactionException.class, () -> transactionServices.anulation(anulationRequest));
		verify(transactionRepository).findById(transactionId);
		verify(transactionRepository, never()).save(any());
	}

	@Test
	void testAnulation_ExpiredTime_Exception() {

		var transactionId = UUID.randomUUID();
		var cardId = "123";
		var transaction = new Transaction();
		var card = new Card();
		card.setCardNumber(cardId);
		transaction.setCard(card);
		transaction.setState(ETransactionState.SUCCESS);
		transaction.setCreateDate(LocalDateTime.now().minusHours(Constants.MAX_HOURS_ANULATION_VALUE + 1));

		var anulationRequest = new AnulationTransactionRequest();
		anulationRequest.setCardId(cardId);
		anulationRequest.setTransactionId(transactionId);
		when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

		assertThrows(ExpiredTimeException.class, () -> transactionServices.anulation(anulationRequest));
		verify(transactionRepository).findById(transactionId);
		verify(transactionRepository, never()).save(any());
	}
}
