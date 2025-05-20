package br.ifsp.consulta_facil_api.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Paciente extends Usuario {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private List<Consulta> historico;

	public Paciente(String nome, String email, String senha, Long id, List<Consulta> historico) {
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
