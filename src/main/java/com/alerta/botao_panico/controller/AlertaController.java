package com.alerta.botao_panico.controller;

import com.alerta.botao_panico.dto.AlertaRequest;
import com.alerta.botao_panico.model.Alerta;
import com.alerta.botao_panico.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    @PostMapping
    public ResponseEntity<Alerta> criarAlerta(@RequestBody AlertaRequest request) {
        Alerta alerta = alertaService.criarAlerta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(alerta);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Alerta>> listarAlertasAtivos() {
        List<Alerta> alertas = alertaService.listarAlertasAtivos();
        return ResponseEntity.ok(alertas);
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<Alerta> resolverAlerta(@PathVariable Long id) {
        Alerta alerta = alertaService.resolverAlerta(id);
        return ResponseEntity.ok(alerta);
    }

    @GetMapping
    public ResponseEntity<List<Alerta>> listarTodosAlertas() {
        List<Alerta> alertas = alertaService.listarTodosAlertas();
        return ResponseEntity.ok(alertas);
    }
}