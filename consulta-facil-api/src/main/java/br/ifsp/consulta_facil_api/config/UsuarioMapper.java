package br.ifsp.consulta_facil_api.config;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.ifsp.consulta_facil_api.dto.AdministradorDTO;
import br.ifsp.consulta_facil_api.dto.PacienteDTO;
import br.ifsp.consulta_facil_api.dto.ProfissionalDTO;
import br.ifsp.consulta_facil_api.dto.UsuarioDTO;
import br.ifsp.consulta_facil_api.model.Administrador;
import br.ifsp.consulta_facil_api.model.Paciente;
import br.ifsp.consulta_facil_api.model.Profissional;
import br.ifsp.consulta_facil_api.model.Usuario;



@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Usuario toEntity(UsuarioDTO dto) {
        switch (dto.getRole()) {
            case PACIENTE:
                return modelMapper.map(dto, Paciente.class);
            case ADMINISTRADOR:
                return modelMapper.map(dto, Administrador.class);
            case PROFISSIONAL:
                return modelMapper.map(dto, Profissional.class);
            default:
                throw new IllegalArgumentException("Role desconhecida: " + dto.getRole());
        }
    }
    
    public UsuarioDTO toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        if (usuario instanceof Paciente paciente) {
            return modelMapper.map(paciente, PacienteDTO.class);
        } else if (usuario instanceof Administrador admin) {
            return modelMapper.map(admin, AdministradorDTO.class);
        } else if (usuario instanceof Profissional prof) {
            return modelMapper.map(prof, ProfissionalDTO.class);
        } else {
            // Mapeia para o DTO base para o caso de algum outro tipo futuro
            return modelMapper.map(usuario, UsuarioDTO.class);
        }
    }
}

