package co.com.novatec.bankinc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import co.com.novatec.bankinc.entity.Client;
import co.com.novatec.bankinc.repository.ClientRepository;
import co.com.novatec.bankinc.services.ClientServices;
import net.datafaker.Faker;

@DataJpaTest
@TestPropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientServicesTest {

	@Mock
	private ClientRepository clientRepository;

	@Test
	void testGenerateNewClient() {

		MockitoAnnotations.openMocks(this);

		var faker = new Faker();

		var clientServices = new ClientServices(faker, clientRepository);

		// Datos de ejemplo
		var name = faker.name().firstName();
		var lastName = faker.name().lastName();
		var expectedClient = Client.builder().name(name).lastName(lastName).build();

		// Configurar comportamiento de ClientRepository
		when(clientRepository.save(any(Client.class))).thenReturn(expectedClient);

		// Generar nuevo cliente
		var result = clientServices.generateNewClient();

		// Verificar el resultado y las interacciones
		assertEquals(expectedClient, result);
		assertEquals(expectedClient.getName(), result.getName());

		verify(clientRepository).save(any(Client.class));


	}

}
