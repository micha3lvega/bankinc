package co.com.novatec.bankinc.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import co.com.novatec.bankinc.controller.CardController;
import co.com.novatec.bankinc.entity.Card;
import co.com.novatec.bankinc.request.AddBalanceRequest;
import co.com.novatec.bankinc.request.CardActiveRequest;
import co.com.novatec.bankinc.response.GetBalanceResponse;
import co.com.novatec.bankinc.services.CardServices;

@SpringBootTest
class CardControllerTest {

	private CardController cardController;

	@Mock
	private CardServices cardServices;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		cardController = new CardController(cardServices);
	}

	@Test
	void testGetCard() {

		Long productId = 123L;
		var card = new Card();
		when(cardServices.generateCard(productId)).thenReturn(card);

		var result = cardController.getCard(productId);

		verify(cardServices, times(1)).generateCard(productId);
		Assertions.assertEquals(card, result);

	}

	@Test
	void testGetBalance() {
		var cardId = "1234567890";
		var response = new GetBalanceResponse();
		when(cardServices.getBalance(cardId)).thenReturn(response);

		var result = cardController.getBalance(cardId);

		verify(cardServices, times(1)).getBalance(cardId);
		Assertions.assertEquals(response, result);
	}

	@Test
	void testEnroll() {
		var request = new CardActiveRequest();
		var card = new Card();
		when(cardServices.enroll(request)).thenReturn(card);

		var result = cardController.enroll(request);

		verify(cardServices, times(1)).enroll(request);
		Assertions.assertEquals(card, result);
	}

	@Test
	void testReloadBalanceCard() {
		var request = new AddBalanceRequest();
		var card = new Card();
		when(cardServices.reloadBalanceCard(request)).thenReturn(card);

		var result = cardController.reloadBalanceCard(request);

		verify(cardServices, times(1)).reloadBalanceCard(request);
		Assertions.assertEquals(card, result);
	}

	@Test
	void testBlockCard() {
		var cardId = "1234567890";
		var card = new Card();
		when(cardServices.blockCard(cardId)).thenReturn(card);

		var result = cardController.blockCard(cardId);

		verify(cardServices, times(1)).blockCard(cardId);
		Assertions.assertEquals(card, result);
	}

}