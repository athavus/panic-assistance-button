package com.alerta.botao_panico.exception;

public class MatriculaJaExisteException extends RuntimeException {
    public MatriculaJaExisteException(String matricula) {
        super("Já existe um professor com a matrícula: " + matricula);
    }
}
