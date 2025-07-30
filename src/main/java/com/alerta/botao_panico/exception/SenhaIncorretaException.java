package com.alerta.botao_panico.exception;

public class SenhaIncorretaException extends RuntimeException {
    public SenhaIncorretaException() {
        super("Senha incorreta");
    }
}
