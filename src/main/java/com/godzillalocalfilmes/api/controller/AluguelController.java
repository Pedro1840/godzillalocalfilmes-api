package com.godzillalocalfilmes.api.controller;

import com.godzillalocalfilmes.api.model.Aluguel;
import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.service.AluguelService;
import com.godzillalocalfilmes.api.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        try {
            Aluguel aluguel = aluguelService.alugarFilme(cliente, filmeId);
            return ResponseEntity.ok(aluguel);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/devolver")
    public ResponseEntity<String> devolverFilme(@RequestParam Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        try {
            aluguelService.devolverFilme(cliente);
            return ResponseEntity.ok("Filme devolvido com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
