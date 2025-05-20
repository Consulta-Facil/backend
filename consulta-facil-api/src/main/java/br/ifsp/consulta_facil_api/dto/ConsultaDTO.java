package br.ifsp.consulta_facil_api.dto;

import java.time.LocalDateTime;

public class ConsultaDTO {
private Long id;
	
	private LocalDateTime dataHora;
	private PacienteDTO paciente;
	private ProfissionalDTO profissional;
	
	public ConsultaDTO(Long id, LocalDateTime dataHora, PacienteDTO paciente, ProfissionalDTO profissional) {
		super();
		this.id = id;
		this.dataHora = dataHora;
		this.paciente = paciente;
		this.profissional = profissional;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public PacienteDTO getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteDTO paciente) {
		this.paciente = paciente;
	}

	public ProfissionalDTO getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalDTO profissional) {
		this.profissional = profissional;
	}
	
	
	
	
}
