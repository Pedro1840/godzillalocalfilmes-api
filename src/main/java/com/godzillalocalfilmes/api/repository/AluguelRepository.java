package com.godzillalocalfilmes.api.repository;

import com.godzillalocalfilmes.api.model.Aluguel;
import com.godzillalocalfilmes.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    Optional<Aluguel> findByCliente(Cliente cliente);
}
