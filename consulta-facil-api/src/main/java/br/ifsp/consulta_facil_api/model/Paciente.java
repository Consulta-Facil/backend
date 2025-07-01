package br.ifsp.consulta_facil_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Usuario {
	
	private String cpf;
	private String telefone;
    private Integer numero;
    private Integer rua;
    private String cidade;
    private String estado;
    private String cep;
    private String uf;
    private String foto;
	
    

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Consulta> historico = new ArrayList<>();
	
}
