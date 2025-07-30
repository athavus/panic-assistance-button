package com.alerta.botao_panico.dto;

import com.alerta.botao_panico.model.TipoAlerta;
import lombok.Data;

@Data
public class AlertaRequest {
    private Long salaId;
    private Long professorId;
    private TipoAlerta tipo;
}
