package co.com.novatec.bankinc.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import co.com.novatec.bankinc.controller.TransactionController;
import co.com.novatec.bankinc.entity.Transaction;
import co.com.novatec.bankinc.request.AnulationTransactionRequest;
import co.com.novatec.bankinc.request.PurchaseRequest;
import co.com.novatec.bankinc.services.TransactionServices;

@SpringBootTest
class TransactionControllerTest {

	@Mock
	private TransactionServices transactionServices;

	private TransactionController transactionController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		transactionController = new TransactionController(transactionServices);
	}

	@Test
	void testFindById() {
		var transactionId = UUID.randomUUID();
		var expectedTransaction = new Transaction();
		when(transactionServices.findById(transactionId)).thenReturn(expectedTransaction);

		var result = transactionController.findById(transactionId);

		assertEquals(expectedTransaction, result);
		verify(transactionServices).findById(transactionId);
	}

	@Test
	void testPurchase() {
		var purchaseRequest = new PurchaseRequest();
		var expectedTransaction = new Transaction();
		when(transactionServices.purchase(purchaseRequest)).thenReturn(expectedTransaction);

		var result = transactionController.purchase(purchaseRequest);

		assertEquals(expectedTransaction, result);
		verify(transactionServices).purchase(purchaseRequest);
	}

	@Test
	void testAnulation() {
		var anulationRequest = new AnulationTransactionRequest();
		var expectedTransaction = new Transaction();
		when(transactionServices.anulation(anulationRequest)).thenReturn(expectedTransaction);

		var result = transactionController.anulation(anulationRequest);

		assertEquals(expectedTransaction, result);
		verify(transactionServices).anulation(anulationRequest);
	}
}
