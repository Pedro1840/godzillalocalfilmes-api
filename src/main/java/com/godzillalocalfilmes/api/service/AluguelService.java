package com.godzillalocalfilmes.api.service;

import com.godzillalocalfilmes.api.exception.CustomException;
import com.godzillalocalfilmes.api.model.Aluguel;
import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.model.Filme;
import com.godzillalocalfilmes.api.repository.AluguelRepository;
import com.godzillalocalfilmes.api.repository.FilmeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final FilmeRepository filmeRepository;

    public AluguelService(AluguelRepository aluguelRepository, FilmeRepository filmeRepository) {
        this.aluguelRepository = aluguelRepository;
        this.filmeRepository = filmeRepository;
    }

    @Transactional
    public Aluguel alugarFilme(Cliente cliente, Long filmeId) {
        if (aluguelRepository.findByCliente(cliente).isPresent()) {
            throw new CustomException("Cliente já possui um filme alugado.");
        }

        Filme filme = filmeRepository.findById(filmeId)
                .orElseThrow(() -> new CustomException("Filme não encontrado."));

        if (filme.getEstoque() <= 0) {
            throw new CustomException("Filme indisponível para aluguel.");
        }

        filme.setEstoque(filme.getEstoque() - 1);
        filmeRepository.save(filme);

        Aluguel aluguel = new Aluguel();
        aluguel.setCliente(cliente);
        aluguel.setFilme(filme);

        return aluguelRepository.save(aluguel);
    }

    @Transactional
    public void devolverFilme(Cliente cliente) {
        Aluguel aluguel = aluguelRepository.findByCliente(cliente)
                .orElseThrow(() -> new CustomException("Cliente não possui filmes alugados."));

        Filme filme = aluguel.getFilme();
        filme.setEstoque(filme.getEstoque() + 1);
        filmeRepository.save(filme);

        aluguelRepository.delete(aluguel);
    }
}
