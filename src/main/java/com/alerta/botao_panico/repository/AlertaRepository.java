package com.alerta.botao_panico.repository;

import com.alerta.botao_panico.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByAtivoTrueOrderByDataHoraDesc();
    List<Alerta> findBySalaIdAndAtivoTrue(Long salaId);
}
