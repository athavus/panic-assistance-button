package com.alerta.botao_panico.controller;

import com.alerta.botao_panico.model.Sala;
import com.alerta.botao_panico.service.SalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService salaService;

    @GetMapping
    public ResponseEntity<List<Sala>> listarSalas() {
        List<Sala> salas = salaService.listarSalas();
        return ResponseEntity.ok(salas);
    }

    @PostMapping
    public ResponseEntity<Sala> cadastrarSala(@RequestBody Sala sala) {
        Sala salaCadastrada = salaService.cadastrarSala(sala);
        return ResponseEntity.status(HttpStatus.CREATED).body(salaCadastrada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscarSala(@PathVariable Long id) {
        Sala sala = salaService.buscarSala(id);
        return ResponseEntity.ok(sala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> atualizarSala(@PathVariable Long id, @RequestBody Sala sala) {
        Sala salaAtualizada = salaService.atualizarSala(id, sala);
        return ResponseEntity.ok(salaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id) {
        salaService.deletarSala(id);
        return ResponseEntity.noContent().build();
    }
}