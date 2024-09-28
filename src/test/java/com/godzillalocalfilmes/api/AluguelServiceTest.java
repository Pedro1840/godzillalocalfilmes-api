package com.godzillalocalfilmes.api;

import com.godzillalocalfilmes.api.exception.CustomException;
import com.godzillalocalfilmes.api.model.Aluguel;
import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.model.Filme;
import com.godzillalocalfilmes.api.repository.AluguelRepository;
import com.godzillalocalfilmes.api.repository.FilmeRepository;
import com.godzillalocalfilmes.api.service.AluguelService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AluguelServiceTest {

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private FilmeRepository filmeRepository;

    @InjectMocks
    private AluguelService aluguelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Teste para alugarFilme com sucesso
    @Test
    public void testAlugarFilme_Sucesso() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEmail("cliente@example.com");

        Filme filme = new Filme();
        filme.setFilmesId(1L);
        filme.setTitulo("Godzilla");
        filme.setEstoque(5);

        Aluguel aluguelEsperado = new Aluguel();
        aluguelEsperado.setCliente(cliente);
        aluguelEsperado.setFilme(filme);

        when(aluguelRepository.findByCliente(cliente)).thenReturn(Optional.empty());
        when(filmeRepository.findById(1L)).thenReturn(Optional.of(filme));
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);
        when(aluguelRepository.save(any(Aluguel.class))).thenReturn(aluguelEsperado);

        Aluguel resultado = aluguelService.alugarFilme(cliente, 1L);

        assertNotNull(resultado);
        assertEquals(cliente, resultado.getCliente());
        assertEquals(filme, resultado.getFilme());
        assertEquals(4, filme.getEstoque()); // Verifica se o estoque foi decrementado
        verify(aluguelRepository, times(1)).findByCliente(cliente);
        verify(filmeRepository, times(1)).findById(1L);
        verify(filmeRepository, times(1)).save(filme);
        verify(aluguelRepository, times(1)).save(any(Aluguel.class));
    }

    // Teste para alugarFilme quando o cliente já possui um aluguel
    @Test
    public void testAlugarFilme_ClienteJaPossuiAluguel() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Aluguel aluguelExistente = new Aluguel();
        aluguelExistente.setCliente(cliente);

        when(aluguelRepository.findByCliente(cliente)).thenReturn(Optional.of(aluguelExistente));

        CustomException exception = assertThrows(CustomException.class, () -> {
            aluguelService.alugarFilme(cliente, 1L);
        });

        assertEquals("Cliente já possui um filme alugado.", exception.getMessage());
        verify(aluguelRepository, times(1)).findByCliente(cliente);
        verifyNoMoreInteractions(filmeRepository);
    }

    // Teste para alugarFilme quando o filme não é encontrado
    @Test
    public void testAlugarFilme_FilmeNaoEncontrado() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(aluguelRepository.findByCliente(cliente)).thenReturn(Optional.empty());
        when(filmeRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            aluguelService.alugarFilme(cliente, 1L);
        });

        assertEquals("Filme não encontrado.", exception.getMessage());
        verify(aluguelRepository, times(1)).findByCliente(cliente);
        verify(filmeRepository, times(1)).findById(1L);
    }

    // Teste para alugarFilme quando o filme está sem estoque
    @Test
    public void testAlugarFilme_FilmeSemEstoque() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Filme filme = new Filme();
        filme.setFilmesId(1L);
        filme.setTitulo("Godzilla");
        filme.setEstoque(0);

        when(aluguelRepository.findByCliente(cliente)).thenReturn(Optional.empty());
        when(filmeRepository.findById(1L)).thenReturn(Optional.of(filme));

        CustomException exception = assertThrows(CustomException.class, () -> {
            aluguelService.alugarFilme(cliente, 1L);
        });

        assertEquals("Filme indisponível para aluguel.", exception.getMessage());
        verify(aluguelRepository, times(1)).findByCliente(cliente);
        verify(filmeRepository, times(1)).findById(1L);
    }

    // Teste para devolverFilme com sucesso
    @Test
    public void testDevolverFilme_Sucesso() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Filme filme = new Filme();
        filme.setFilmesId(1L);
        filme.setTitulo("Godzilla");
        filme.setEstoque(4);

        Aluguel aluguel = new Aluguel();
        aluguel.setCliente(cliente);
        aluguel.setFilme(filme);

        when(aluguelRepository.findByCliente(cliente)).thenReturn(Optional.of(aluguel));
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);
        doNothing().when(aluguelRepository).delete(aluguel);

        aluguelService.devolverFilme(cliente);

        assertEquals(5, filme.getEstoque()); // Verifica se o estoque foi incrementado
        verify(aluguelRepository, times(1)).findByCliente(cliente);
        verify(filmeRepository, times(1)).save(filme);
        verify(aluguelRepository, times(1)).delete(aluguel);
    }

    // Teste para devolverFilme quando o cliente não possui aluguel
    @Test
    public void testDevolverFilme_ClienteSemAluguel() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(aluguelRepository.findByCliente(cliente)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            aluguelService.devolverFilme(cliente);
        });

        assertEquals("Cliente não possui filmes alugados.", exception.getMessage());
        verify(aluguelRepository, times(1)).findByCliente(cliente);
        verifyNoMoreInteractions(filmeRepository);
    }
}
