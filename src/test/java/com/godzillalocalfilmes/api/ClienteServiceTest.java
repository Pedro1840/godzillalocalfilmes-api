package com.godzillalocalfilmes.api;

import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.model.Role;
import com.godzillalocalfilmes.api.repository.ClienteRepository;
import com.godzillalocalfilmes.api.repository.RoleRepository;
import com.godzillalocalfilmes.api.service.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Teste para registrarCliente
    @Test
    public void testRegistrarCliente() {
        Cliente cliente = new Cliente();
        cliente.setEmail("teste@example.com");
        cliente.setNome("Teste");
        cliente.setSenha("senha123");

        String senhaCriptografada = "senhaCriptografada";

        when(passwordEncoder.encode("senha123")).thenReturn(senhaCriptografada);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.registrarCliente(cliente);

        assertEquals(senhaCriptografada, cliente.getSenha());
        assertEquals(cliente, result);
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(clienteRepository, times(1)).save(cliente);
    }

    // Teste para buscarPorEmail
    @Test
    public void testBuscarPorEmail() {
        String email = "teste@example.com";
        Cliente cliente = new Cliente();
        cliente.setEmail(email);

        when(clienteRepository.findByEmail(email)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.buscarPorEmail(email);

        assertTrue(result.isPresent());
        assertEquals(cliente, result.get());
        verify(clienteRepository, times(1)).findByEmail(email);
    }

    // Teste para buscarPorId
    @Test
    public void testBuscarPorId() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(id);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.buscarPorId(id);

        assertTrue(result.isPresent());
        assertEquals(cliente, result.get());
        verify(clienteRepository, times(1)).findById(id);
    }

    // Teste para verificarSenha
    @Test
    public void testVerificarSenha() {
        String senhaEntrada = "senha123";
        String senhaHash = "senhaCriptografada";

        when(passwordEncoder.matches(senhaEntrada, senhaHash)).thenReturn(true);

        boolean result = clienteService.verificarSenha(senhaEntrada, senhaHash);

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(senhaEntrada, senhaHash);
    }

    // Teste para atribuirRole
    @Test
    public void testAtribuirRole() {
        Cliente cliente = new Cliente();
        cliente.setRoles(new java.util.HashSet<>());

        String roleName = "ROLE_ADMIN";
        Role role = new Role();
        role.setName(roleName);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        clienteService.atribuirRole(cliente, roleName);

        assertTrue(cliente.getRoles().contains(role));
        verify(roleRepository, times(1)).findByName(roleName);
        verify(clienteRepository, times(1)).save(cliente);
    }

    // Teste para atribuirRole com Role não encontrada
    @Test
    public void testAtribuirRole_RoleNotFound() {
        Cliente cliente = new Cliente();
        String roleName = "ROLE_ADMIN";

        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.atribuirRole(cliente, roleName);
        });

        assertEquals("Role não encontrada: " + roleName, exception.getMessage());
        verify(roleRepository, times(1)).findByName(roleName);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // Teste para removerRole
    @Test
    public void testRemoverRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        Cliente cliente = new Cliente();
        cliente.setRoles(new java.util.HashSet<>(Set.of(role)));

        String roleName = "ROLE_USER";

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        clienteService.removerRole(cliente, roleName);

        assertFalse(cliente.getRoles().contains(role));
        verify(roleRepository, times(1)).findByName(roleName);
        verify(clienteRepository, times(1)).save(cliente);
    }

    // Teste para removerRole com Role não encontrada
    @Test
    public void testRemoverRole_RoleNotFound() {
        Cliente cliente = new Cliente();
        String roleName = "ROLE_USER";

        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.removerRole(cliente, roleName);
        });

        assertEquals("Role não encontrada: " + roleName, exception.getMessage());
        verify(roleRepository, times(1)).findByName(roleName);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // Teste para deleteCliente
    @Test
    public void testDeleteCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        doNothing().when(clienteRepository).deleteById(cliente.getId());

        clienteService.deleteCliente(cliente);

        verify(clienteRepository, times(1)).deleteById(cliente.getId());
    }
}
