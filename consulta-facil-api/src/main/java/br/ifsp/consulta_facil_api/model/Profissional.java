package br.ifsp.consulta_facil_api.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Profissional extends Usuario {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String especialidade;
	private List<Horario> horariosDisponiveis;


	public Profissional(String nome, String email, String senha, Long id, String especialidade,
			List<Horario> horariosDisponiveis) {
		super(nome, email, senha);
		this.id = id;
		this.especialidade = especialidade;
		this.horariosDisponiveis = horariosDisponiveis;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public List<Horario> getHorariosDisponiveis() {
		return horariosDisponiveis;
	}

	public void setHorariosDisponiveis(List<Horario> horariosDisponiveis) {
		this.horariosDisponiveis = horariosDisponiveis;
	}
	
	
	
	
	
	
	
	
}
