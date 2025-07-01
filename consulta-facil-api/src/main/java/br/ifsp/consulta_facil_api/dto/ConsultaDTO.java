package br.ifsp.consulta_facil_api.dto;

import java.time.LocalDateTime;

import br.ifsp.consulta_facil_api.model.StatusConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
	private Long id;
	
	@NotNull(message = "Horário é obrigatório")
	@Future(message = "Horário deve ser no futuro")
	private LocalDateTime horario;
	
	@NotNull(message = "Paciente é obrigatório")
	private PacienteDTO paciente;
	
	@NotNull(message = "Profissional é obrigatório")
	private ProfissionalDTO profissional;
	
	@Size(max = 500, message = "Observações não podem ter mais de 500 caracteres")
	private String observacoes;
	
	private StatusConsulta statusConsulta;
}

