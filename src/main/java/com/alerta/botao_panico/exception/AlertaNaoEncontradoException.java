package com.alerta.botao_panico.exception;

public class AlertaNaoEncontradoException extends RuntimeException {
    public AlertaNaoEncontradoException(Long id) {
        super("Alerta não encontrado com ID: " + id);
    }
}
