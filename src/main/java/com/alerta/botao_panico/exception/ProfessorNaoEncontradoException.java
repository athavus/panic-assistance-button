package com.alerta.botao_panico.exception;

public class ProfessorNaoEncontradoException extends RuntimeException {
    public ProfessorNaoEncontradoException(Long id) {
        super("Professor não encontrado com ID: " + id);
    }
}
