package com.godzillalocalfilmes.api.controller;

import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.service.AluguelService;
import com.godzillalocalfilmes.api.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/godzilla")
public class AluguelController {

    private final AluguelService aluguelService;
    private final ClienteService clienteService;

    public AluguelController(AluguelService aluguelService, ClienteService clienteService) {
        this.aluguelService = aluguelService;
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> alugarFilme(@RequestParam Long clienteId, @RequestParam Long filmeId) {
        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();

        Cliente authenticatedCliente = clienteService.buscarPorEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        // Verifica se o clienteId passado corresponde ao cliente autenticado
        if (!authenticatedCliente.getId().equals(clienteId)) {
            return ResponseEntity.status(403).body("Aluguel negado: cliente não corresponde ao usuário autenticado.");
        }
        try {
            aluguelService.alugarFilme(authenticatedCliente, filmeId);
            return ResponseEntity.ok().body("Filme alugado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/devolver")
    public ResponseEntity<String> devolverFilme(@RequestParam Long clienteId) {
        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();

        Cliente authenticatedCliente = clienteService.buscarPorEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        // Verifica se o clienteId passado corresponde ao cliente autenticado
        if (!authenticatedCliente.getId().equals(clienteId)) {
            return ResponseEntity.status(403).body("Devolução negada: cliente não corresponde ao usuário autenticado.");
        }

        try {
            aluguelService.devolverFilme(authenticatedCliente);
            return ResponseEntity.ok("Filme devolvido com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
