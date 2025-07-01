package br.ifsp.consulta_facil_api.repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.ifsp.consulta_facil_api.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long>, PagingAndSortingRepository<Consulta, Long> {
	Page<Consulta> findByPacienteId(Long pacienteId, Pageable pageable);
    Page<Consulta> findByProfissionalId(Long profissionalId, Pageable pageable);
    
    boolean existsByProfissionalIdAndHorario(Long profissionalId, LocalDateTime horario);
    
    Page<Consulta> findByPacienteIdAndHorarioBefore(Long idPaciente, LocalDateTime horario, Pageable pageable);
    Page<Consulta> findByProfissionalIdAndHorarioBetween(Long idProfissional, LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
    Page<Consulta> findByProfissionalIdAndPacienteId(Long idProfissional, Long idPaciente, Pageable pageable);
    Page<Consulta> findByHorarioBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);



}
