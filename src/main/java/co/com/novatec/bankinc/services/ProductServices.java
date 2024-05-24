package co.com.novatec.bankinc.services;

import org.springframework.stereotype.Service;

import co.com.novatec.bankinc.entity.Product;
import co.com.novatec.bankinc.exception.NoActiveException;
import co.com.novatec.bankinc.exception.NotFoundException;
import co.com.novatec.bankinc.repository.ProductRepository;

/**
 * Servicio para realizar operaciones relacionadas con productos.
 */
@Service
public class ProductServices {

	private ProductRepository productRepository;

	/**
	 * Estado inactivo de un producto.
	 */
	public static final int INACTIVE_STATE = 0;

	/**
	 * Crea una instancia del servicio de productos.
	 *
	 * @param productRepository Repositorio de productos.
	 */
	public ProductServices(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Busca un producto por su ID y verifica si está activo.
	 *
	 * @param id ID del producto a buscar.
	 * @return El producto encontrado.
	 * @throws NotFoundException Si no se encuentra el producto con el ID especificado.
	 * @throws NoActiveException Si el producto está inactivo.
	 */
	public Product findProductById(Long id) {
		var product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

		if (product.getState() == INACTIVE_STATE) {
			throw new NoActiveException("Producto no activo");
		}

		return product;
	}
}

