package br.ifsp.consulta_facil_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.ifsp.consulta_facil_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, PagingAndSortingRepository<Usuario, Long> {

}
