package br.ifsp.consulta_facil_api.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Horario {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private LocalDate dataHoraInicio;
	
	private LocalDate dataHoraFim;

	public Horario(Long id, LocalDate dataHoraInicio, LocalDate dataHoraFim) {
		super();
		this.id = id;
		this.dataHoraInicio = dataHoraInicio;
		this.dataHoraFim = dataHoraFim;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(LocalDate dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public LocalDate getDataHoraFim() {
		return dataHoraFim;
	}

	public void setDataHoraFim(LocalDate dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}
	
	
	
	
}
