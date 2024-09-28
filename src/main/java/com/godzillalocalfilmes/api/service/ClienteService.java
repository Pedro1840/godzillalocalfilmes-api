package com.godzillalocalfilmes.api.service;

import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.model.Role;
import com.godzillalocalfilmes.api.repository.ClienteRepository;
import com.godzillalocalfilmes.api.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cliente registrarCliente(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public boolean verificarSenha(String senhaEntrada, String senhaHash) {
        return passwordEncoder.matches(senhaEntrada, senhaHash);
    }

    // Método para atribuir roles a um cliente
    public void atribuirRole(Cliente cliente, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName));
        cliente.getRoles().add(role);
        clienteRepository.save(cliente);
    }

    // Método para remover roles de um cliente
    public void removerRole(Cliente cliente, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName));
        cliente.getRoles().remove(role);
        clienteRepository.save(cliente);
    }

    public void deleteCliente(Cliente cliente) {
        clienteRepository.deleteById(cliente.getId());
    }
}
