package br.ifsp.consulta_facil_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.ifsp.consulta_facil_api.model.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Long>, PagingAndSortingRepository<Administrador, Long> {
	Page<Administrador> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
