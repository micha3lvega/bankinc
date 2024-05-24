package co.com.novatec.bankinc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.novatec.bankinc.entity.Card;

public interface CardRepository extends JpaRepository<Card, String>{

}
