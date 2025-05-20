package br.ifsp.consulta_facil_api.dto;

public class AdministradorDTO extends UsuarioDTO {
	private Long id;

	public AdministradorDTO(String nome, String email, String senha, Long id) {
		super(nome, email, senha);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
