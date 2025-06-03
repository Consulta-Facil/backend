package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.ProfissionalDTO;
import br.ifsp.consulta_facil_api.model.Profissional;
import br.ifsp.consulta_facil_api.repository.ProfissionalRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class ProfissionalService {

	@Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<ProfissionalDTO> listar(Pageable pageable) {
        return profissionalRepository.findAll(pageable)
                .map(profissional -> modelMapper.map(profissional, ProfissionalDTO.class));
    }

    public Optional<ProfissionalDTO> buscarPorId(Long id) {
        return profissionalRepository.findById(id)
                .map(profissional -> modelMapper.map(profissional, ProfissionalDTO.class));
    }

    public ProfissionalDTO salvar(ProfissionalDTO dto) {
        Profissional profissional = modelMapper.map(dto, Profissional.class);
        Profissional salvo = profissionalRepository.save(profissional);
        return modelMapper.map(salvo, ProfissionalDTO.class);
    }

    public void deletar(Long id) {
        profissionalRepository.deleteById(id);
    }
}
