package br.ifsp.consulta_facil_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorDTO extends UsuarioDTO {
	private String cpf;
	private String telefone;
    private Integer numero;
    private Integer rua;
    private String cidade;
    private String estado;
    private String cep;
    private String uf;
	private String foto;

}
