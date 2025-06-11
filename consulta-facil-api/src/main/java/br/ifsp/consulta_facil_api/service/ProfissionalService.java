package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.ProfissionalDTO;
import br.ifsp.consulta_facil_api.model.Profissional;
import br.ifsp.consulta_facil_api.repository.ProfissionalRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {

	@Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    public ProfissionalDTO criarProfissional(ProfissionalDTO dto) {

        Profissional profissional = modelMapper.map(dto, Profissional.class);

        if (dto.getSenha() != null) {
            profissional.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Profissional salvo = profissionalRepository.save(profissional);
        return modelMapper.map(salvo, ProfissionalDTO.class);
    }

    public ProfissionalDTO atualizarProfissional(Long id, ProfissionalDTO dto) {
        Profissional profissional = profissionalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        profissional.setNome(dto.getNome());
        profissional.setEmail(dto.getEmail());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            profissional.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Profissional atualizado = profissionalRepository.save(profissional);
        return modelMapper.map(atualizado, ProfissionalDTO.class);
    }
    
    public List<Profissional> listarComHorariosDisponiveis() {
        return profissionalRepository.findAll().stream()
            .filter(p -> p.getHorariosDisponiveis()
                         .stream()
                         .anyMatch(h -> !h.isDisponivel()))
            .collect(Collectors.toList());
    }

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
