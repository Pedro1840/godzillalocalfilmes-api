package com.godzillalocalfilmes.api.controller;

import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.model.Role;
import com.godzillalocalfilmes.api.service.ClienteService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ClienteService clienteService;

    public AdminController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clientes")
    public ResponseEntity<?> criarCliente(@RequestBody Cliente cliente) {
        if (clienteService.buscarPorEmail(cliente.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Email já está em uso.");
        }

        // Atribui a role USER por padrão
        cliente.getRoles().add(new Role(1L, "ROLE_USER"));

        clienteService.registrarCliente(cliente);
        return ResponseEntity.ok("Cliente criado com sucesso!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clientes/atribuir-role")
    public ResponseEntity<?> atribuirRole(@RequestParam Long clienteId, @RequestParam String role) {

        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        try {
            // Atribui a role escolhida (privilegio)
            clienteService.atribuirRole(cliente, role);

            return ResponseEntity.ok("Privilégio atribuido ao Cliente com sucesso!");
        } catch (Exception e) {
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clientes/remover-role")
    public ResponseEntity<?> removerRole(@RequestParam Long clienteId, @RequestParam String role) {

        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        try {
            // remove a role escolhida (privilegio)
            clienteService.removerRole(cliente, role);

            return ResponseEntity.ok("Privilégio Removido do Cliente com sucesso!");
        } catch (Exception e) {
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clientes/delete/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable("id") String id) {

        Cliente cliente = clienteService.buscarPorId(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        try {
            // deleta um cliente do banco
            clienteService.deleteCliente(cliente);

            return ResponseEntity.ok("Cliente deletado com sucesso!");
        } catch (Exception e) {
            throw e;
        }
    }
}
