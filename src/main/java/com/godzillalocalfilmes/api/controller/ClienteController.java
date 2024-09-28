package com.godzillalocalfilmes.api.controller;

import com.godzillalocalfilmes.api.dto.AuthenticationResponse;
import com.godzillalocalfilmes.api.dto.ClienteDTO;
import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.model.Role;
import com.godzillalocalfilmes.api.security.JwtTokenProvider;
import com.godzillalocalfilmes.api.service.ClienteService;

import java.util.Optional;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class ClienteController {

    private final ClienteService clienteService;
    private final JwtTokenProvider tokenProvider;

    public ClienteController(ClienteService clienteService, JwtTokenProvider tokenProvider) {
        this.clienteService = clienteService;
        this.tokenProvider = tokenProvider;
    }

    // Endpoint para registrar um novo cliente
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCliente(@RequestBody Cliente cliente) {
        // Verifica se o email já está em uso
        if (clienteService.buscarPorEmail(cliente.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Email já está em uso.");
        }

        // Atribui a role USER por padrão
        cliente.getRoles().add(new Role(2L, "ROLE_USER"));

        clienteService.registrarCliente(cliente);
        return ResponseEntity.ok("Cliente registrado com sucesso!");
    }

    // Endpoint para login do cliente
    @PostMapping("/usuario")
    public ResponseEntity<?> loginCliente(@RequestBody Cliente cliente) {
        Optional<Cliente> clienteOptional = clienteService.buscarPorEmail(cliente.getEmail());

        if (clienteOptional.isPresent()) {
            Cliente clienteEncontrado = clienteOptional.get();

            // Verificar se a senha corresponde
            boolean senhaCorreta = clienteService.verificarSenha(cliente.getSenha(), clienteEncontrado.getSenha());

            if (senhaCorreta) {
                String token = tokenProvider.generateToken(clienteEncontrado.getEmail(),
                        List.copyOf(clienteEncontrado.getRoles()));
                ClienteDTO clienteDTO = new ClienteDTO(
                        clienteEncontrado.getId(),
                        clienteEncontrado.getEmail(),
                        clienteEncontrado.getNome());
                return ResponseEntity.ok(new AuthenticationResponse(true, clienteDTO, token));
            } else {
                return ResponseEntity.status(401).body("Senha incorreta.");
            }
        } else {
            return ResponseEntity.status(404).body("Cliente não encontrado.");
        }
    }
}
