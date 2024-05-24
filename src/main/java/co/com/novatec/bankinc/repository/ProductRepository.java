package co.com.novatec.bankinc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.novatec.bankinc.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
