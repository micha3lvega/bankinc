package co.com.novatec.bankinc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import co.com.novatec.bankinc.entity.Card;
import co.com.novatec.bankinc.entity.Client;
import co.com.novatec.bankinc.entity.Product;
import co.com.novatec.bankinc.enums.ECardStates;
import co.com.novatec.bankinc.exception.InvalidStateCardException;
import co.com.novatec.bankinc.exception.NotFoundException;
import co.com.novatec.bankinc.repository.CardRepository;
import co.com.novatec.bankinc.request.AddBalanceRequest;
import co.com.novatec.bankinc.request.CardActiveRequest;
import co.com.novatec.bankinc.services.CardServices;
import co.com.novatec.bankinc.services.ClientServices;
import co.com.novatec.bankinc.services.ProductServices;
import net.datafaker.Faker;

@DataJpaTest
@TestPropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CardServicesTest {

	private Faker faker;
	private ClientServices clientServices;
	private ProductServices productServices;
	private CardRepository cardRepository;
	private CardServices cardServices;

	@BeforeEach
	public void setUp() {
		faker = new Faker();
		clientServices = mock(ClientServices.class);
		productServices = mock(ProductServices.class);
		cardRepository = mock(CardRepository.class);
		cardServices = new CardServices(faker, clientServices, productServices, cardRepository);
	}

	@Test
	void testGenerateCard() {

		// Configurar el comportamiento esperado de los servicios simulados
		var client = new Client();
		when(clientServices.generateNewClient()).thenReturn(client);

		var product = new Product();
		product.setPrefix("12345678");
		when(productServices.findProductById(1L)).thenReturn(product);

		var cardNumber = "1234567890";
		when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
			var card = (Card) invocation.getArgument(0);
			card.setCardNumber(cardNumber);
			return card;
		});

		// Llamar al método y verificar el resultado
		var result = cardServices.generateCard(1L);
		assertNotNull(result);
		assertEquals(client, result.getClient());
		assertEquals(product, result.getProduct());
		assertEquals(cardNumber, result.getCardNumber());
		assertEquals(ECardStates.INACTIVE, result.getState());

		// Verificar que se llamaron a los métodos correspondientes de los servicios simulados
		verify(clientServices).generateNewClient();
		verify(productServices).findProductById(1L);
		verify(cardRepository).save(any(Card.class));
	}

	@Test
	void testEnroll() {
		// Configurar el comportamiento esperado del repositorio simulado
		var card = new Card();
		card.setState(ECardStates.INACTIVE);
		when(cardRepository.save(any(Card.class))).thenReturn(card);
		when(cardRepository.findById("123")).thenReturn(Optional.of(card));

		// Llamar al método y verificar el resultado
		var request = new CardActiveRequest();
		request.setCardId("123");
		var result = cardServices.enroll(request);
		assertNotNull(result);
		assertEquals(ECardStates.ACTIVE, result.getState());

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
		// Verificar que se llamó al método save del repositorio
		verify(cardRepository).save(card);
	}

	@Test
	void testBlockCard() {
		// Configurar el comportamiento esperado del repositorio simulado
		var card = new Card();
		card.setState(ECardStates.ACTIVE);
		when(cardRepository.findById("123")).thenReturn(Optional.of(card));
		when(cardRepository.save(any(Card.class))).thenReturn(card);

		// Llamar al método y verificar el resultado
		var result = cardServices.blockCard("123");
		assertNotNull(result);
		assertEquals(ECardStates.BLOCKED, result.getState());

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
		// Verificar que se llamó al método save del repositorio
		verify(cardRepository).save(card);
	}

	@Test
	void testReloadBalanceCard_CardNotFound() {
		// Configurar el comportamiento esperado del repositorio simulado
		when(cardRepository.findById("123")).thenReturn(Optional.empty());

		// Llamar al método y verificar que lanza la excepción NotFoundException
		var request = new AddBalanceRequest();
		request.setCardId("123");
		request.setBalance("100");
		assertThrows(NotFoundException.class, () -> {
			cardServices.reloadBalanceCard(request);
		});

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
		// Verificar que no se llamó al método save del repositorio
		verify(cardRepository, never()).save(any(Card.class));
	}

	@Test
	void testReloadBalanceCard_CardInactive() {
		// Configurar el comportamiento esperado del repositorio simulado
		var card = new Card();
		card.setState(ECardStates.INACTIVE);
		when(cardRepository.findById("123")).thenReturn(Optional.of(card));

		// Llamar al método y verificar que lanza la excepción InvalidStateCardException
		var request = new AddBalanceRequest();
		request.setCardId("123");
		request.setBalance("100");
		assertThrows(InvalidStateCardException.class, () -> {
			cardServices.reloadBalanceCard(request);
		});

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
		// Verificar que no se llamó al método save del repositorio
		verify(cardRepository, never()).save(any(Card.class));
	}

	@Test
	void testReloadBalanceCard_ValidRequest() {

		// Configurar el comportamiento esperado del repositorio simulado
		var card = new Card();
		card.setBalance("100.00");
		card.setState(ECardStates.ACTIVE);
		when(cardRepository.findById("123")).thenReturn(Optional.of(card));
		when(cardRepository.save(any(Card.class))).thenReturn(card);

		// Llamar al método y verificar el resultado
		var request = new AddBalanceRequest();
		request.setCardId("123");
		request.setBalance("50");
		var result = cardServices.reloadBalanceCard(request);

		assertNotNull(result);
		assertEquals("150.000", result.getBalance());

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
		// Verificar que se llamó al método save del repositorio
		verify(cardRepository).save(card);
	}

	@Test
	void testGetBalance_CardNotFound() {

		// Configurar el comportamiento esperado del repositorio simulado
		when(cardRepository.findById("123")).thenReturn(Optional.empty());

		// Llamar al método y verificar que lanza la excepción NotFoundException
		assertThrows(NotFoundException.class, () -> {
			cardServices.getBalance("123");
		});

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
	}

	@Test
	void testGetBalance_CardFound() {
		// Configurar el comportamiento esperado del repositorio simulado
		var card = new Card();
		card.setBalance("100.00");
		card.setState(ECardStates.ACTIVE);
		card.setCardNumber("1234567812345678");
		when(cardRepository.findById("123")).thenReturn(Optional.of(card));

		// Llamar al método y verificar el resultado
		var result = cardServices.getBalance("123");
		assertNotNull(result);
		assertEquals("100.00", result.getBalance());

		// Configurar el retorno cuando es guarda
		when(cardRepository.save(any(Card.class))).thenReturn(card);

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
	}

	@Test
	void testFindCardByID_CardFound() {
		// Configurar el comportamiento esperado del repositorio simulado
		var card = new Card();
		when(cardRepository.findById("123")).thenReturn(Optional.of(card));

		// Llamar al método y verificar el resultado
		var result = cardServices.findCardByID("123");
		assertNotNull(result);
		assertSame(card, result);

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
	}

	@Test
	void testFindCardByID_CardNotFound() {
		// Configurar el comportamiento esperado del repositorio simulado
		when(cardRepository.findById("123")).thenReturn(Optional.empty());

		// Llamar al método y verificar que lanza la excepción NotFoundException
		assertThrows(NotFoundException.class, () -> {
			cardServices.findCardByID("123");
		});

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
	}

	@Test
	void testUpdate_CardNull() {
		// Llamar al método y verificar que lanza la excepción IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> {
			cardServices.update(null);
		});

		// Verificar que no se llamó al método findById del repositorio
		verify(cardRepository, never()).findById(anyString());
		// Verificar que no se llamó al método save del repositorio
		verify(cardRepository, never()).save(any(Card.class));
	}

	@Test
	void testUpdate_CardNotFound() {
		// Configurar el comportamiento esperado del repositorio simulado
		when(cardRepository.findById("123")).thenReturn(Optional.empty());

		// Llamar al método y verificar que lanza la excepción NotFoundException
		var card = new Card();
		card.setCardNumber("123");
		assertThrows(NotFoundException.class, () -> {
			cardServices.update(card);
		});

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
		// Verificar que no se llamó al método save del repositorio
		verify(cardRepository, never()).save(any(Card.class));
	}

	@Test
	void testUpdate_CardFound() {
		// Configurar el comportamiento esperado del repositorio simulado
		var card = new Card();
		card.setCardNumber("123");
		card.setState(ECardStates.ACTIVE);
		when(cardRepository.findById("123")).thenReturn(Optional.of(card));
		when(cardRepository.save(any(Card.class))).thenReturn(card);

		// Llamar al método y verificar el resultado
		var updatedCard = new Card();
		updatedCard.setCardNumber("123");
		updatedCard.setState(ECardStates.ACTIVE);
		var result = cardServices.update(updatedCard);

		assertNotNull(result);
		assertEquals(updatedCard, result);

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(cardRepository).findById("123");
		// Verificar que se llamó al método save del repositorio
		verify(cardRepository).save(updatedCard);
	}

}
