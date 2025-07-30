package com.alerta.botao_panico.exception;

public class SalaNaoEncontradaException extends RuntimeException {
    public SalaNaoEncontradaException(Long id) {
        super("Sala não encontrada com ID: " + id);
    }
}
