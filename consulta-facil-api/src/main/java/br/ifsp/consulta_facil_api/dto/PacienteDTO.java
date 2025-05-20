package br.ifsp.consulta_facil_api.dto;

import java.util.List;

import br.ifsp.consulta_facil_api.model.Consulta;

public class PacienteDTO extends UsuarioDTO {
	private Long id;
	private List<Consulta> historico;
	
	
	public PacienteDTO(String nome, String email, String senha, Long id, List<Consulta> historico) {
		super(nome, email, senha);
		this.id = id;
		this.historico = historico;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public List<Consulta> getHistorico() {
		return historico;
	}


	public void setHistorico(List<Consulta> historico) {
		this.historico = historico;
	}
	
	
	
	
}
