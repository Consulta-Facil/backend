package br.ifsp.consulta_facil_api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO extends UsuarioDTO {
	
	private String cpf;
	private String telefone;
    private Integer numero;
    private Integer rua;
    private String cidade;
    private String estado;
    private String cep;
    private String uf;
    private String foto;
	private List<ConsultaDTO> historico;
	
	
}
