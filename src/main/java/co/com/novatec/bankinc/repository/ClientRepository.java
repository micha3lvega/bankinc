package co.com.novatec.bankinc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.novatec.bankinc.entity.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

}
