package com.alerta.botao_panico.service;

import com.alerta.botao_panico.dto.AlertaRequest;
import com.alerta.botao_panico.exception.AlertaNaoEncontradoException;
import com.alerta.botao_panico.exception.ProfessorNaoEncontradoException;
import com.alerta.botao_panico.exception.SalaNaoEncontradaException;
import com.alerta.botao_panico.model.Alerta;
import com.alerta.botao_panico.factory.AlertaFactory;
import com.alerta.botao_panico.model.Professor;
import com.alerta.botao_panico.model.Sala;
import com.alerta.botao_panico.repository.AlertaRepository;
import com.alerta.botao_panico.repository.ProfessorRepository;
import com.alerta.botao_panico.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final SalaRepository salaRepository;
    private final ProfessorRepository professorRepository;
    private final AlertaFactory alertaFactory;

    public Alerta criarAlerta(AlertaRequest request) {
        Sala sala = salaRepository.findById(request.getSalaId())
                .orElseThrow(() -> new SalaNaoEncontradaException(request.getSalaId()));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new ProfessorNaoEncontradoException(request.getProfessorId()));

        Alerta alerta = alertaFactory.criarAlerta(request, sala, professor);

        return alertaRepository.save(alerta);
    }

    @Transactional(readOnly = true)
    public List<Alerta> listarAlertasAtivos() {
        return alertaRepository.findByAtivoTrueOrderByDataHoraDesc();
    }

    public Alerta resolverAlerta(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new AlertaNaoEncontradoException(id));

        alerta.setAtivo(false);
        return alertaRepository.save(alerta);
    }

    @Transactional(readOnly = true)
    public List<Alerta> listarTodosAlertas() {
        return alertaRepository.findAll();
    }
}