package com.alerta.botao_panico.service;

import com.alerta.botao_panico.dto.LoginRequest;
import com.alerta.botao_panico.dto.ProfessorRequest;
import com.alerta.botao_panico.dto.ProfessorResponse;
import com.alerta.botao_panico.exception.MatriculaJaExisteException;
import com.alerta.botao_panico.exception.ProfessorNaoEncontradoException;
import com.alerta.botao_panico.exception.ProfessorNaoEncontradoPorMatriculaException;
import com.alerta.botao_panico.exception.SenhaIncorretaException;
import com.alerta.botao_panico.model.Professor;
import com.alerta.botao_panico.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public List<ProfessorResponse> listarProfessores() {
        return professorRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public ProfessorResponse cadastrarProfessor(ProfessorRequest request) {
        // Verifica se já existe professor com a mesma matrícula
        if (professorRepository.existsByMatricula(request.getMatricula())) {
            throw new MatriculaJaExisteException(request.getMatricula());
        }

        // Criptografa a senha
        String senhaCriptografada = passwordEncoder.encode(request.getSenha());

        Professor professor = Professor.builder()
                .nome(request.getNome())
                .matricula(request.getMatricula())
                .senha(senhaCriptografada)
                .build();

        Professor professorSalvo = professorRepository.save(professor);
        return converterParaResponse(professorSalvo);
    }

    @Transactional(readOnly = true)
    public ProfessorResponse buscarProfessor(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNaoEncontradoException(id));
        return converterParaResponse(professor);
    }

    @Transactional(readOnly = true)
    public ProfessorResponse loginProfessor(LoginRequest loginRequest) {
        Professor professor = professorRepository.findByMatricula(loginRequest.getMatricula())
                .orElseThrow(() -> new ProfessorNaoEncontradoPorMatriculaException(loginRequest.getMatricula()));

        if (!passwordEncoder.matches(loginRequest.getSenha(), professor.getSenha())) {
            throw new SenhaIncorretaException();
        }

        return converterParaResponse(professor);
    }

    public ProfessorResponse atualizarProfessor(Long id, ProfessorRequest request) {
        Professor professorExistente = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNaoEncontradoException(id));

        // Verifica se a nova matrícula já existe (se foi alterada)
        if (!professorExistente.getMatricula().equals(request.getMatricula())
                && professorRepository.existsByMatricula(request.getMatricula())) {
            throw new MatriculaJaExisteException(request.getMatricula());
        }

        // Atualiza os campos
        if (request.getNome() != null) {
            professorExistente.setNome(request.getNome());
        }
        if (request.getMatricula() != null) {
            professorExistente.setMatricula(request.getMatricula());
        }
        if (request.getSenha() != null) {
            String senhaCriptografada = passwordEncoder.encode(request.getSenha());
            professorExistente.setSenha(senhaCriptografada);
        }

        Professor professorAtualizado = professorRepository.save(professorExistente);
        return converterParaResponse(professorAtualizado);
    }

    public void deletarProfessor(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNaoEncontradoException(id));
        professorRepository.delete(professor);
    }

    private ProfessorResponse converterParaResponse(Professor professor) {
        return ProfessorResponse.builder()
                .id(professor.getId())
                .nome(professor.getNome())
                .matricula(professor.getMatricula())
                .build();
    }
}