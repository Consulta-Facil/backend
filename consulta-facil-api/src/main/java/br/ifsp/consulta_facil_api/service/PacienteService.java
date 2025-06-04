package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.PacienteDTO;
import br.ifsp.consulta_facil_api.model.Paciente;
import br.ifsp.consulta_facil_api.repository.PacienteRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PacienteService {

	@Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<PacienteDTO> listar(Pageable pageable) {
        return pacienteRepository.findAll(pageable)
                .map(paciente -> modelMapper.map(paciente, PacienteDTO.class));
    }

    public Optional<PacienteDTO> buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(paciente -> modelMapper.map(paciente, PacienteDTO.class));
    }
    
    public PacienteDTO atualizarDados(Long idLogado, PacienteDTO dto) {
        Paciente existente = pacienteRepository.findById(idLogado)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        
        modelMapper.map(dto, existente);

        Paciente atualizado = pacienteRepository.save(existente);
        return modelMapper.map(atualizado, PacienteDTO.class);
    }

    public PacienteDTO salvar(PacienteDTO dto) {
        Paciente paciente = modelMapper.map(dto, Paciente.class);
        Paciente salvo = pacienteRepository.save(paciente);
        return modelMapper.map(salvo, PacienteDTO.class);
    }

    public void deletar(Long id) {
        pacienteRepository.deleteById(id);
    }
}
