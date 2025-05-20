package br.ifsp.consulta_facil_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.ifsp.consulta_facil_api.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long>, PagingAndSortingRepository<Horario, Long>{

}
