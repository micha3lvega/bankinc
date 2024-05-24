package co.com.novatec.bankinc.builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.com.novatec.bankinc.builder.CardBuilder;
import co.com.novatec.bankinc.entity.Card;
import co.com.novatec.bankinc.entity.Client;
import co.com.novatec.bankinc.entity.Product;
import co.com.novatec.bankinc.enums.ECardStates;

class CardBuilderTest {

	private CardBuilder cardBuilder;

	@BeforeEach
	public void setUp() {
		cardBuilder = new CardBuilder();
	}

	@Test
	void testBuild() {

		var cardNumber = "1234567890";
		var stringBalance = "1000.000";

		var client = new Client();
		var product = new Product();
		var cardState = ECardStates.ACTIVE;
		var createDate = LocalDateTime.now();
		var expirationDate = LocalDate.now();
		var balance = new BigDecimal(stringBalance);
		var lastModifiedDate = LocalDateTime.now();

		var expectedCard = new Card(cardNumber, client, cardState, product, stringBalance, createDate, expirationDate,
				lastModifiedDate);

		var card = cardBuilder.cardNumber(cardNumber).client(client).cardState(cardState).product(product)
				.balance(balance).createDate(createDate).expirationDate(expirationDate)
				.lastModifiedDate(lastModifiedDate).active().build();

		Assertions.assertEquals(expectedCard, card);
		Assertions.assertEquals(expectedCard.getClient(), card.getClient());
		Assertions.assertEquals(expectedCard.getBalance(), card.getBalance());
		Assertions.assertEquals(expectedCard.getCardNumber(), card.getCardNumber());
		Assertions.assertEquals(expectedCard.getCreateDate(), card.getCreateDate());
		Assertions.assertEquals(expectedCard.getExpirationDate(), card.getExpirationDate());
		Assertions.assertEquals(expectedCard.getLastModifiedDate(), card.getLastModifiedDate());
		Assertions.assertEquals(expectedCard.getProduct(), card.getProduct());
		Assertions.assertEquals(expectedCard.getState(), card.getState());
		Assertions.assertEquals(ECardStates.ACTIVE, expectedCard.getState());

	}

	@Test
	void testLastModifiedDate() {
		var lastModifiedDate = LocalDateTime.of(2022, 1, 1, 10, 30, 0);
		var card = cardBuilder.lastModifiedDate(lastModifiedDate).build();

		Assertions.assertEquals(lastModifiedDate, card.getLastModifiedDate());
	}

	@Test
	void testInactive() {

		var card = new CardBuilder().inactive().build();

		var expectCard = new Card();
		expectCard.setState(ECardStates.INACTIVE);

		// Validar que el builder cree una tarjeta en estado inactivo
		Assertions.assertEquals(ECardStates.INACTIVE, card.getState());

		// Validar que los estados de una tarjeta generada con el builder sea igual a la
		// generada desde el objecto
		Assertions.assertEquals(card.getState(), expectCard.getState());

	}

	@Test
	void testActive() {

		var card = new CardBuilder().active().build();

		var expectCard = new Card();
		expectCard.setState(ECardStates.ACTIVE);

		// Validar que el builder cree una tarjeta en estado activa
		Assertions.assertEquals(ECardStates.ACTIVE, card.getState());

		// Validar que los estados de una tarjeta generada con el builder sea igual a la
		// generada desde el objecto
		Assertions.assertEquals(card.getState(), expectCard.getState());

	}

	@Test
	void testBlocked() {

		var card = new CardBuilder().blocked().build();

		var expectCard = new Card();
		expectCard.setState(ECardStates.BLOCKED);

		// Validar que el builder cree una tarjeta en estado activa
		Assertions.assertEquals(ECardStates.BLOCKED, card.getState());

		// Validar que los estados de una tarjeta generada con el builder sea igual a la
		// generada desde el objecto
		Assertions.assertEquals(card.getState(), expectCard.getState());

	}

}
