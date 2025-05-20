package br.ifsp.consulta_facil_api.dto;

import java.time.LocalDate;

public class HorarioDTO {
	private Long id;
	private LocalDate dataHoraInicio;
	private LocalDate dataHoraFim;
	
	
	public HorarioDTO(Long id, LocalDate dataHoraInicio, LocalDate dataHoraFim) {
		super();
		this.id = id;
		this.dataHoraInicio = dataHoraInicio;
		this.dataHoraFim = dataHoraFim;
	}
	
	

}
