package co.com.novatec.bankinc.services;

import org.springframework.stereotype.Service;

import co.com.novatec.bankinc.entity.Client;
import co.com.novatec.bankinc.repository.ClientRepository;
import net.datafaker.Faker;

/**
 * Servicio para generar y gestionar clientes.
 */
@Service
public class ClientServices {

	private Faker faker;
	private ClientRepository clientRepository;

	/**
	 * Crea una instancia del servicio de clientes.
	 *
	 * @param faker            Instancia de Faker para generar datos aleatorios.
	 * @param clientRepository Repositorio de clientes.
	 */
	public ClientServices(Faker faker, ClientRepository clientRepository) {
		this.faker = faker;
		this.clientRepository = clientRepository;
	}

	/**
	 * Genera un nuevo cliente con nombre y apellidos al azar y lo guarda en la base de datos.
	 *
	 * @return El cliente generado y guardado en la base de datos.
	 */
	public Client generateNewClient() {

		// Generar cliente
		var name = faker.name().firstName();
		var lastName = faker.name().lastName();
		var client = Client.builder().name(name).lastName(lastName).build();

		// Guardar cliente
		return clientRepository.save(client);

	}

}
