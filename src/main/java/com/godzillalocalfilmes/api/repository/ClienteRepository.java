package com.godzillalocalfilmes.api.repository;

import com.godzillalocalfilmes.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<Cliente> findByEmail(String email);
}
