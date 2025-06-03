package br.ifsp.consulta_facil_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.ifsp.consulta_facil_api.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long>, PagingAndSortingRepository<Profissional, Long>  {
	Page<Profissional> findByEspecialidadeContainingIgnoreCase(String especialidade, Pageable pageable);
    Page<Profissional> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
