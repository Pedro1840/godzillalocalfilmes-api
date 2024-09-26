package com.godzillalocalfilmes.api.service;

import com.godzillalocalfilmes.api.model.Filme;
import com.godzillalocalfilmes.api.repository.FilmeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmeService {

    private final FilmeRepository filmeRepository;

    public FilmeService(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    public List<Filme> listarTodos() {
        return filmeRepository.findAll();
    }

    public Optional<Filme> buscarPorId(Long id) {
        return filmeRepository.findById(id);
    }

    public List<Filme> buscarPorTitulo(String titulo) {
        return filmeRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Filme> buscarPorAno(int ano) {
        return filmeRepository.findByAno(ano);
    }

    public Filme salvar(Filme filme) {
        return filmeRepository.save(filme);
    }
}