package br.ifsp.consulta_facil_api.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrador extends Usuario {

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
