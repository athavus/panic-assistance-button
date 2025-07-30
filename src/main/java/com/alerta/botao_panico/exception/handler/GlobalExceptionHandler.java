package com.alerta.botao_panico.exception.handler;

import com.alerta.botao_panico.exception.AlertaNaoEncontradoException;
import com.alerta.botao_panico.exception.MatriculaJaExisteException;
import com.alerta.botao_panico.exception.ProfessorNaoEncontradoException;
import com.alerta.botao_panico.exception.ProfessorNaoEncontradoPorMatriculaException;
import com.alerta.botao_panico.exception.SalaNaoEncontradaException;
import com.alerta.botao_panico.exception.SenhaIncorretaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ========================================
    // EXCEÇÕES DE ENTIDADES NÃO ENCONTRADAS
    // ========================================

    @ExceptionHandler(SalaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleSalaNaoEncontrada(SalaNaoEncontradaException ex) {
        return criarRespostaErro(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProfessorNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleProfessorNaoEncontrado(ProfessorNaoEncontradoException ex) {
        return criarRespostaErro(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProfessorNaoEncontradoPorMatriculaException.class)
    public ResponseEntity<Map<String, Object>> handleProfessorNaoEncontradoPorMatricula(ProfessorNaoEncontradoPorMatriculaException ex) {
        return criarRespostaErro(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlertaNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleAlertaNaoEncontrado(AlertaNaoEncontradoException ex) {
        return criarRespostaErro(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // ========================================
    // EXCEÇÕES DE AUTENTICAÇÃO E AUTORIZAÇÃO
    // ========================================

    @ExceptionHandler(SenhaIncorretaException.class)
    public ResponseEntity<Map<String, Object>> handleSenhaIncorreta(SenhaIncorretaException ex) {
        return criarRespostaErro(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // ========================================
    // EXCEÇÕES DE CONFLITO DE DADOS
    // ========================================

    @ExceptionHandler(MatriculaJaExisteException.class)
    public ResponseEntity<Map<String, Object>> handleMatriculaJaExiste(MatriculaJaExisteException ex) {
        return criarRespostaErro(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensagem = "Violação de integridade de dados";

        // Trata especificamente violação de unique constraint em salas
        if (ex.getMessage().contains("CONSTRAINT_INDEX_4") && ex.getMessage().contains("SALAS")) {
            mensagem = "Já existe uma sala com este nome";
        }
        // Trata especificamente violação de unique constraint em professores
        else if (ex.getMessage().contains("PROFESSORES") && ex.getMessage().contains("MATRICULA")) {
            mensagem = "Já existe um professor com esta matrícula";
        }

        return criarRespostaErro(mensagem, HttpStatus.CONFLICT);
    }

    // ========================================
    // EXCEÇÕES DE ARGUMENTOS INVÁLIDOS
    // ========================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return criarRespostaErro(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ========================================
    // EXCEÇÃO GENÉRICA PARA CASOS NÃO TRATADOS
    // ========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        // Log do erro para debug (opcional)
        System.err.println("Erro não tratado: " + ex.getMessage());
        ex.printStackTrace();

        return criarRespostaErro("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========================================
    // MÉTODO UTILITÁRIO PARA CRIAR RESPOSTA
    // ========================================

    private ResponseEntity<Map<String, Object>> criarRespostaErro(String mensagem, HttpStatus status) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", status.value());
        erro.put("error", status.getReasonPhrase());
        erro.put("message", mensagem);

        return ResponseEntity.status(status).body(erro);
    }
}