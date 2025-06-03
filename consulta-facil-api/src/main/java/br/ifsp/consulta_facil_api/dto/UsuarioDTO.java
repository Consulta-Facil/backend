package br.ifsp.consulta_facil_api.dto;

import br.ifsp.consulta_facil_api.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	private String senha;
	
	@NotNull
    private Role role;
	
}
