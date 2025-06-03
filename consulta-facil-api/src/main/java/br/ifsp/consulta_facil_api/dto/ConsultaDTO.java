package br.ifsp.consulta_facil_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
	private Long id;
	private LocalDateTime dataHora;
	private PacienteDTO paciente;
	private ProfissionalDTO profissional;

	
}
