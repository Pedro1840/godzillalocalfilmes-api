package com.godzillalocalfilmes.api;

import com.godzillalocalfilmes.api.model.Filme;
import com.godzillalocalfilmes.api.repository.FilmeRepository;
import com.godzillalocalfilmes.api.service.FilmeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilmeServiceTest {

    @Mock
    private FilmeRepository filmeRepository;

    @InjectMocks
    private FilmeService filmeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Teste para listarTodos
    @Test
    public void testListarTodos() {
        Filme filme1 = new Filme();
        filme1.setFilmesId(1L);
        filme1.setTitulo("Filme 1");

        Filme filme2 = new Filme();
        filme2.setFilmesId(2L);
        filme2.setTitulo("Filme 2");

        List<Filme> filmesMock = Arrays.asList(filme1, filme2);

        when(filmeRepository.findAll()).thenReturn(filmesMock);

        List<Filme> resultado = filmeService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(filme1, resultado.get(0));
        assertEquals(filme2, resultado.get(1));
        verify(filmeRepository, times(1)).findAll();
    }

    // Teste para buscarPorId quando o filme é encontrado
    @Test
    public void testBuscarPorId_FilmeEncontrado() {

        Long id = 1L;
        Filme filme = new Filme();
        filme.setFilmesId(id);
        filme.setTitulo("Godzilla");

        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Optional<Filme> resultado = filmeService.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(filme, resultado.get());
        verify(filmeRepository, times(1)).findById(id);
    }

    // Teste para buscarPorId quando o filme não é encontrado
    @Test
    public void testBuscarPorId_FilmeNaoEncontrado() {
        Long id = 1L;

        when(filmeRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Filme> resultado = filmeService.buscarPorId(id);

        assertFalse(resultado.isPresent());
        verify(filmeRepository, times(1)).findById(id);
    }

    // Teste para buscarPorTitulo
    @Test
    public void testBuscarPorTitulo() {
        String titulo = "Godzilla";

        Filme filme1 = new Filme();
        filme1.setFilmesId(1L);
        filme1.setTitulo("Godzilla vs Kong");

        Filme filme2 = new Filme();
        filme2.setFilmesId(2L);
        filme2.setTitulo("Godzilla: Rei dos Monstros");

        List<Filme> filmesMock = Arrays.asList(filme1, filme2);

        when(filmeRepository.findByTituloContainingIgnoreCase(titulo)).thenReturn(filmesMock);

        List<Filme> resultado = filmeService.buscarPorTitulo(titulo);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(filme1));
        assertTrue(resultado.contains(filme2));
        verify(filmeRepository, times(1)).findByTituloContainingIgnoreCase(titulo);
    }

    // Teste para buscarPorAno
    @Test
    public void testBuscarPorAno() {
        int ano = 2021;

        Filme filme1 = new Filme();
        filme1.setFilmesId(1L);
        filme1.setTitulo("Godzilla vs Kong");
        filme1.setAno(2021);

        Filme filme2 = new Filme();
        filme2.setFilmesId(2L);
        filme2.setTitulo("Outro Filme");
        filme2.setAno(2021);

        List<Filme> filmesMock = Arrays.asList(filme1, filme2);

        when(filmeRepository.findByAno(ano)).thenReturn(filmesMock);

        List<Filme> resultado = filmeService.buscarPorAno(ano);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(filme1));
        assertTrue(resultado.contains(filme2));
        verify(filmeRepository, times(1)).findByAno(ano);
    }

    // Teste para salvar
    @Test
    public void testSalvar() {
        Filme filme = new Filme();
        filme.setTitulo("Godzilla vs Kong");

        when(filmeRepository.save(filme)).thenReturn(filme);

        Filme resultado = filmeService.salvar(filme);

        assertEquals(filme, resultado);
        verify(filmeRepository, times(1)).save(filme);
    }
}
