package com.alerta.botao_panico.exception;

public class ProfessorNaoEncontradoPorMatriculaException extends RuntimeException {
    public ProfessorNaoEncontradoPorMatriculaException(String matricula) {
        super("Professor não encontrado com matrícula: " + matricula);
    }
}
