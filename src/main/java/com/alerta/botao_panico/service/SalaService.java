package com.alerta.botao_panico.service;

import com.alerta.botao_panico.exception.SalaNaoEncontradaException;
import com.alerta.botao_panico.model.Sala;
import com.alerta.botao_panico.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaService {

    private final SalaRepository salaRepository;

    @Transactional(readOnly = true)
    public List<Sala> listarSalas() {
        return salaRepository.findAll();
    }

    public Sala cadastrarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    @Transactional(readOnly = true)
    public Sala buscarSala(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new SalaNaoEncontradaException(id));
    }

    public Sala atualizarSala(Long id, Sala salaAtualizada) {
        Sala salaExistente = buscarSala(id);

        // Atualiza apenas os campos necessários
        if (salaAtualizada.getNome() != null) {
            salaExistente.setNome(salaAtualizada.getNome());
        }
        if (salaAtualizada.getBloco() != null) {
            salaExistente.setBloco(salaAtualizada.getBloco());
        }

        return salaRepository.save(salaExistente);
    }

    public void deletarSala(Long id) {
        Sala sala = buscarSala(id);
        salaRepository.delete(sala);
    }
}