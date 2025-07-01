package br.ifsp.consulta_facil_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Profissional extends Usuario {

	

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

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    private List<Horario> horariosDisponiveis;
	
    public Profissional(Long id) {
		super(id);
	}
}
