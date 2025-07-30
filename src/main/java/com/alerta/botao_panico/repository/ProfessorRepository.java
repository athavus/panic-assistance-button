package com.alerta.botao_panico.repository;

import com.alerta.botao_panico.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByMatricula(String matricula);
    boolean existsByMatricula(String matricula);
}