package com.alerta.botao_panico.controller;

import com.alerta.botao_panico.dto.LoginRequest;
import com.alerta.botao_panico.dto.ProfessorRequest;
import com.alerta.botao_panico.dto.ProfessorResponse;
import com.alerta.botao_panico.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professores")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorResponse>> listarProfessores() {
        List<ProfessorResponse> professores = professorService.listarProfessores();
        return ResponseEntity.ok(professores);
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> cadastrarProfessor(@RequestBody ProfessorRequest request) {
        ProfessorResponse professor = professorService.cadastrarProfessor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(professor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponse> buscarProfessor(@PathVariable Long id) {
        ProfessorResponse professor = professorService.buscarProfessor(id);
        return ResponseEntity.ok(professor);
    }

    @PostMapping("/login")
    public ResponseEntity<ProfessorResponse> loginProfessor(@RequestBody LoginRequest loginRequest) {
        ProfessorResponse professor = professorService.loginProfessor(loginRequest);
        return ResponseEntity.ok(professor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponse> atualizarProfessor(@PathVariable Long id, @RequestBody ProfessorRequest request) {
        ProfessorResponse professor = professorService.atualizarProfessor(id, request);
        return ResponseEntity.ok(professor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable Long id) {
        professorService.deletarProfessor(id);
        return ResponseEntity.noContent().build();
    }
}