package br.ifsp.consulta_facil_api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalDTO extends UsuarioDTO {
	private String registro;
	private String especialidade;
    
    private String cpf;
    private String telefone;
    private Integer numero;
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private String uf;
    private String foto;
    
    @JsonIgnore // Evita recursão
    private List<HorarioDTO> horariosDisponiveis;

	
}
