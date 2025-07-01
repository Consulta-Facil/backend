package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.HorarioDTO;
import br.ifsp.consulta_facil_api.model.Horario;
import br.ifsp.consulta_facil_api.model.Profissional;
import br.ifsp.consulta_facil_api.repository.HorarioRepository;
import br.ifsp.consulta_facil_api.repository.ProfissionalRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<HorarioDTO> listar(Pageable pageable) {
        return horarioRepository.findAll(pageable)
                .map(horario -> modelMapper.map(horario, HorarioDTO.class));
    }

    public Optional<HorarioDTO> buscarPorId(Long id) {
        return horarioRepository.findById(id)
                .map(horario -> modelMapper.map(horario, HorarioDTO.class));
    }

    public HorarioDTO salvar(HorarioDTO dto) {
        Profissional profissional = profissionalRepository.findById(dto.getProfissional().getId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        Horario horario = modelMapper.map(dto, Horario.class);
        horario.setProfissional(profissional);
        horario = horarioRepository.save(horario);
        return modelMapper.map(horario, HorarioDTO.class);
    }
    
    public HorarioDTO atualizar(Long id, HorarioDTO dto) {
        Horario horario = horarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Horário não encontrado"));

        horario.setDisponivel(dto.isDisponivel());

        if (dto.getProfissional() != null && dto.getProfissional().getId() != null) {
            Profissional profissional = profissionalRepository.findById(dto.getProfissional().getId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
            horario.setProfissional(profissional);
        }

        horario = horarioRepository.save(horario);

        return modelMapper.map(horario, HorarioDTO.class);
    }

    public void deletar(Long id) {
        horarioRepository.deleteById(id);
    }

    public Page<HorarioDTO> listarPorProfissional(Long profissionalId, Pageable pageable) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        return horarioRepository.findByProfissional(profissional, pageable)
                .map(horario -> modelMapper.map(horario, HorarioDTO.class));
    }

    public Page<HorarioDTO> listarHorariosDisponiveis(Long profissionalId, Pageable pageable) {
        return horarioRepository
            .findByProfissionalIdAndDisponivelTrue(profissionalId, pageable)
            .map(horario -> modelMapper.map(horario, HorarioDTO.class));
    }
}
