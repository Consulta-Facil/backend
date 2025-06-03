package br.ifsp.consulta_facil_api.repository;



import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.ifsp.consulta_facil_api.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long>, PagingAndSortingRepository<Horario, Long>{
	Page<Horario> findByProfissionalId(Long profissionalId, Pageable pageable);
    Page<Horario> findByDataHoraInicio(LocalDate data, Pageable pageable);

}
