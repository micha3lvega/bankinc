package co.com.novatec.bankinc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import co.com.novatec.bankinc.entity.Product;
import co.com.novatec.bankinc.exception.NoActiveException;
import co.com.novatec.bankinc.exception.NotFoundException;
import co.com.novatec.bankinc.repository.ProductRepository;
import co.com.novatec.bankinc.services.ProductServices;

@SpringBootTest
class ProductServicesTest {

	private ProductRepository productRepository;
	private ProductServices productServices;

	@BeforeEach
	public void setUp() {
		productRepository = mock(ProductRepository.class);
		productServices = new ProductServices(productRepository);
	}

	@Test
	void testFindProductById_ProductFoundAndActive() {
		// Configurar el comportamiento esperado del repositorio simulado
		var product = new Product();
		product.setState(1);
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		// Llamar al método y verificar el resultado
		var result = productServices.findProductById(1L);
		assertEquals(product, result);
		assertEquals(1, result.getState());

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(productRepository).findById(1L);
	}

	@Test
	void testFindProductById_ProductNotFound() {

		// Configurar el comportamiento
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		// Llamar al método y verificar que lanza la excepción NotFoundException
		assertThrows(NotFoundException.class, () -> {
			productServices.findProductById(1L);
		});

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(productRepository).findById(1L);
	}

	@Test
	void testFindProductById_ProductInactive() {

		// Configurar el comportamiento esperado del repositorio simulado
		var product = new Product();
		product.setState(0);
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		// Llamar al método y verificar que lanza la excepción NoActiveException
		assertThrows(NoActiveException.class, () -> {
			productServices.findProductById(1L);
		});

		// Verificar que se llamó al método findById del repositorio con el ID correcto
		verify(productRepository).findById(1L);
	}

}
