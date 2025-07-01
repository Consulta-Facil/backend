package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.ConsultaDTO;
import br.ifsp.consulta_facil_api.model.Consulta;
import br.ifsp.consulta_facil_api.repository.ConsultaRepository;
import br.ifsp.consulta_facil_api.repository.HorarioRepository;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import br.ifsp.consulta_facil_api.exception.ConsultaException;
import br.ifsp.consulta_facil_api.exception.HorarioException;
import br.ifsp.consulta_facil_api.exception.RecursoNaoEncontradoException;
import br.ifsp.consulta_facil_api.exception.CodigosErro;

@Service
public class ConsultaService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

    @Autowired
    private ConsultaRepository consultaRepository;
    
    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<ConsultaDTO> listar(Pageable pageable) {
        logger.info("Listando consultas com paginação: {}", pageable);
        Page<Consulta> consultas = consultaRepository.findAll(pageable);
        logger.debug("Encontradas {} consultas", consultas.getTotalElements());
        return consultas.map(consulta -> modelMapper.map(consulta, ConsultaDTO.class));
    }
    
    public Page<ConsultaDTO> listarHistoricoDoPaciente(Long idPaciente, Pageable pageable) {
        logger.info("Listando histórico do paciente {} com paginação: {}", idPaciente, pageable);
        LocalDateTime agora = LocalDateTime.now();
        return consultaRepository.findByPacienteIdAndHorarioBefore(idPaciente, agora, pageable)
                .map(c -> modelMapper.map(c, ConsultaDTO.class));
    }

    public Page<ConsultaDTO> listarPorData(Long idProfissional, LocalDate data, Pageable pageable) {
        logger.info("Listando consultas do profissional {} na data {} com paginação: {}", idProfissional, data, pageable);
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = inicio.plusDays(1);
        return consultaRepository.findByProfissionalIdAndHorarioBetween(idProfissional, inicio, fim, pageable)
                .map(c -> modelMapper.map(c, ConsultaDTO.class));
    }
    
    public Page<ConsultaDTO> listarPorPaciente(Long idProfissional, Long idPaciente, Pageable pageable) {
        logger.info("Listando consultas do profissional {} com paciente {} com paginação: {}", idProfissional, idPaciente, pageable);
        return consultaRepository.findByProfissionalIdAndPacienteId(idProfissional, idPaciente, pageable)
                .map(c -> modelMapper.map(c, ConsultaDTO.class));
    }

    
    public Page<ConsultaDTO> listarConsultasPorData(LocalDate data, Pageable pageable) {
        logger.info("Listando consultas na data {} com paginação: {}", data, pageable);
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = inicio.plusDays(1);
        return consultaRepository.findByHorarioBetween(inicio, fim, pageable)
                .map(c -> modelMapper.map(c, ConsultaDTO.class));
    }

    public Page<ConsultaDTO> listarConsultasPorProfissional(Long idProfissional, Pageable pageable) {
        logger.info("Listando consultas do profissional {} com paginação: {}", idProfissional, pageable);
        return consultaRepository.findByProfissionalId(idProfissional, pageable)
                .map(c -> modelMapper.map(c, ConsultaDTO.class));
    }



    public Optional<ConsultaDTO> buscarPorId(Long id) {
        logger.info("Buscando consulta com ID: {}", id);
        Optional<Consulta> consulta = consultaRepository.findById(id);
        if (consulta.isPresent()) {
            logger.debug("Consulta encontrada: {}", id);
        } else {
            logger.warn("Consulta não encontrada: {}", id);
        }
        return consulta.map(c -> modelMapper.map(c, ConsultaDTO.class));
    }

    public ConsultaDTO salvar(ConsultaDTO dto) {
        logger.info("Salvando nova consulta para paciente {} com profissional {} no horário {}", 
                   dto.getPaciente().getId(), dto.getProfissional().getId(), dto.getHorario());
        
        Consulta novaConsulta = modelMapper.map(dto, Consulta.class);

        Long profissionalId = novaConsulta.getProfissional().getId();
        LocalDateTime horarioConsulta = novaConsulta.getHorario();

        // Verificar conflito direto na consulta
        boolean conflito = consultaRepository.existsByProfissionalIdAndHorario(profissionalId, horarioConsulta);
        if (conflito) {
            logger.warn("Tentativa de agendamento com conflito - Profissional: {}, Horário: {}", profissionalId, horarioConsulta);
            throw new ConsultaException("Já existe uma consulta agendada com este profissional neste horário.", CodigosErro.CONFLITO_HORARIO);
        }

        // Verificar se o horário está disponível
        boolean horarioDisponivel = horarioRepository
            .findByProfissionalIdAndDisponivelTrue(profissionalId, Pageable.unpaged())
            .stream()
            .anyMatch(h -> 
                horarioConsulta.isEqual(h.getDataHoraInicio()) ||
                (horarioConsulta.isAfter(h.getDataHoraInicio()) && horarioConsulta.isBefore(h.getDataHoraFim()))
            );

        if (!horarioDisponivel) {
            logger.warn("Tentativa de agendamento em horário indisponível - Profissional: {}, Horário: {}", profissionalId, horarioConsulta);
            throw new HorarioException("O horário selecionado não está disponível para este profissional.", CodigosErro.HORARIO_INDISPONIVEL);
        }

        // Salvar a nova consulta
        novaConsulta = consultaRepository.save(novaConsulta);
        logger.info("Consulta salva com sucesso. ID: {}", novaConsulta.getId());

        // Atualizar o horário para indisponível
        horarioRepository.findByProfissionalIdAndDisponivelTrue(profissionalId, Pageable.unpaged())
            .stream()
            .filter(h -> 
                horarioConsulta.isEqual(h.getDataHoraInicio()) ||
                (horarioConsulta.isAfter(h.getDataHoraInicio()) && horarioConsulta.isBefore(h.getDataHoraFim()))
            )
            .findFirst()
            .ifPresent(horario -> {
                horario.setDisponivel(false);
                horarioRepository.save(horario);
                logger.debug("Horário marcado como indisponível. ID: {}", horario.getId());
            });

        return modelMapper.map(novaConsulta, ConsultaDTO.class);
    }


    public void deletar(Long id) {
        logger.info("Tentando deletar consulta com ID: {}", id);
        
        Optional<Consulta> optionalConsulta = consultaRepository.findById(id);

        if (optionalConsulta.isEmpty()) {
            logger.warn("Tentativa de deletar consulta inexistente. ID: {}", id);
            throw new RecursoNaoEncontradoException("Consulta", id);
        }

        Consulta consulta = optionalConsulta.get();

        if (consulta.getHorario().isBefore(LocalDateTime.now().plusHours(24))) {
            logger.warn("Tentativa de cancelamento sem antecedência de 24h. ID: {}, Horário: {}", id, consulta.getHorario());
            throw new ConsultaException("Cancelamentos só são permitidos com pelo menos 24h de antecedência.", CodigosErro.CANCELAMENTO_SEM_ANTECEDENCIA);
        }

        consultaRepository.deleteById(id);
        logger.info("Consulta deletada com sucesso. ID: {}", id);
    }
}
