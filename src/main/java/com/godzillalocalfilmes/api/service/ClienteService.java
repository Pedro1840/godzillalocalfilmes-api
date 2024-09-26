package com.godzillalocalfilmes.api.service;

import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Cliente registrarCliente(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public boolean verificarSenha(String senhaEntrada, String senhaHash) {
        return passwordEncoder.matches(senhaEntrada, senhaHash);
    }
}