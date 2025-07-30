package com.alerta.botao_panico;

import com.alerta.botao_panico.model.Professor;
import com.alerta.botao_panico.model.Sala;
import com.alerta.botao_panico.repository.ProfessorRepository;
import com.alerta.botao_panico.repository.SalaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BotaoPanicoApplication {

	private final SalaRepository salaRepository;
	private final ProfessorRepository professorRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BotaoPanicoApplication.class, args);
	}

	@PostConstruct
	public void initData() {
		if (salaRepository.count() == 0) {
			salaRepository.save(Sala.builder().nome("Sala 101").bloco("A").build());
			salaRepository.save(Sala.builder().nome("Sala 201").bloco("B").build());
			salaRepository.save(Sala.builder().nome("Laboratório 1").bloco("C").build());
			salaRepository.save(Sala.builder().nome("Laboratório 2").bloco("C").build());
			salaRepository.save(Sala.builder().nome("Auditório").bloco("D").build());
		}

		if (professorRepository.count() == 0) {
			professorRepository.save(Professor.builder().nome("João Silva").matricula("12345").senha(passwordEncoder.encode("senha123")).build());
			professorRepository.save(Professor.builder().nome("Maria Santos").matricula("67890").senha(passwordEncoder.encode("senha456")).build());
			professorRepository.save(Professor.builder().nome("Pedro Oliveira").matricula("11111").senha(passwordEncoder.encode("senha000")).build());
			professorRepository.save(Professor.builder().nome("Ana Costa").matricula("22222").senha(passwordEncoder.encode("senha000123")).build());
		}
	}
}
