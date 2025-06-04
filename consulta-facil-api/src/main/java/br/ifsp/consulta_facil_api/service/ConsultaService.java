package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.ConsultaDTO;
import br.ifsp.consulta_facil_api.model.Consulta;
import br.ifsp.consulta_facil_api.repository.ConsultaRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<ConsultaDTO> listar(Pageable pageable) {
        Page<Consulta> consultas = consultaRepository.findAll(pageable);
        return consultas.map(consulta -> modelMapper.map(consulta, ConsultaDTO.class));
    }

    public Optional<ConsultaDTO> buscarPorId(Long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        return consulta.map(c -> modelMapper.map(c, ConsultaDTO.class));
    }

    public ConsultaDTO salvar(ConsultaDTO dto) {
        Consulta novaConsulta = modelMapper.map(dto, Consulta.class);

        // Verificar conflito de agendamento
        boolean conflito = consultaRepository.existsByProfissionalIdAndHorario(
                novaConsulta.getProfissional().getId(),
                novaConsulta.getHorario()
        );

        if (conflito) {
            throw new RuntimeException("Já existe uma consulta agendada com este profissional neste horário.");
        }

        novaConsulta = consultaRepository.save(novaConsulta);
        return modelMapper.map(novaConsulta, ConsultaDTO.class);
    }

    public void deletar(Long id) {
        Optional<Consulta> optionalConsulta = consultaRepository.findById(id);

        if (optionalConsulta.isEmpty()) {
            throw new RuntimeException("Consulta não encontrada.");
        }

        Consulta consulta = optionalConsulta.get();

        if (consulta.getHorario().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RuntimeException("Cancelamentos só são permitidos com pelo menos 24h de antecedência.");
        }

        consultaRepository.deleteById(id);
    }
}
