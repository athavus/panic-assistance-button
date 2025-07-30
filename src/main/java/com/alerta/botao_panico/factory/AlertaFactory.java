package com.alerta.botao_panico.factory;

import com.alerta.botao_panico.dto.AlertaRequest;
import com.alerta.botao_panico.model.Alerta;
import com.alerta.botao_panico.model.Professor;
import com.alerta.botao_panico.model.Sala;
import com.alerta.botao_panico.model.TipoAlerta;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AlertaFactory {

    /**
     * Cria um novo alerta baseado no request, sala e professor
     */
    public Alerta criarAlerta(AlertaRequest request, Sala sala, Professor professor) {
        return Alerta.builder()
                .sala(sala)
                .professor(professor)
                .tipo(request.getTipo())
                .dataHora(LocalDateTime.now())
                .ativo(true)
                .build();
    }

    /**
     * Cria um alerta com parâmetros específicos
     */
    public Alerta criarAlerta(Sala sala, Professor professor, TipoAlerta tipo) {
        return Alerta.builder()
                .sala(sala)
                .professor(professor)
                .tipo(tipo)
                .dataHora(LocalDateTime.now())
                .ativo(true)
                .build();
    }

    /**
     * Cria um alerta para teste com data/hora customizada
     */
    public Alerta criarAlertaParaTeste(Sala sala, Professor professor, TipoAlerta tipo, LocalDateTime dataHora, boolean ativo) {
        return Alerta.builder()
                .sala(sala)
                .professor(professor)
                .tipo(tipo)
                .dataHora(dataHora)
                .ativo(ativo)
                .build();
    }

    /**
     * Cria um alerta inativo (já resolvido)
     */
    public Alerta criarAlertaResolvido(Sala sala, Professor professor, TipoAlerta tipo) {
        return Alerta.builder()
                .sala(sala)
                .professor(professor)
                .tipo(tipo)
                .dataHora(LocalDateTime.now())
                .ativo(false)
                .build();
    }
}