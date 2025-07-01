package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.AdministradorDTO;
import br.ifsp.consulta_facil_api.model.Administrador;
import br.ifsp.consulta_facil_api.repository.AdministradorRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministradorService {

	@Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<AdministradorDTO> listar(Pageable pageable) {
        Page<Administrador> administradores = administradorRepository.findAll(pageable);
        return administradores.map(administrador -> modelMapper.map(administrador, AdministradorDTO.class));
    }

    public Optional<AdministradorDTO> buscarPorId(Long id) {
        Optional<Administrador> administrador = administradorRepository.findById(id);
        return administrador.map(adm -> modelMapper.map(adm, AdministradorDTO.class));
    }

    public AdministradorDTO salvar(AdministradorDTO dto) {
        Administrador administrador = modelMapper.map(dto, Administrador.class);
        administrador = administradorRepository.save(administrador);
        return modelMapper.map(administrador, AdministradorDTO.class);
    }

    public void deletar(Long id) {
        administradorRepository.deleteById(id);
    }
}
