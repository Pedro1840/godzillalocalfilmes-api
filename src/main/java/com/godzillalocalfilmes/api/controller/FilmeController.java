package com.godzillalocalfilmes.api.controller;

import com.godzillalocalfilmes.api.model.Filme;
import com.godzillalocalfilmes.api.service.FilmeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/localdora/godzilla")
public class FilmeController {

    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @GetMapping
    public ResponseEntity<List<Filme>> buscarPorTitulo(@RequestParam String titulo) {
        List<Filme> filmes = filmeService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(filmes);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Filme>> listarTodos() {
        return ResponseEntity.ok(filmeService.listarTodos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Filme> adicionarFilme(@RequestBody Filme filme) {
        return ResponseEntity.ok(filmeService.salvar(filme));
    }
}
