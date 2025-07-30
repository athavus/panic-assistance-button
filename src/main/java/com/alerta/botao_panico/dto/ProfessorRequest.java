package com.alerta.botao_panico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorRequest {
    private String nome;
    private String matricula;
    private String senha;
}