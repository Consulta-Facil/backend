package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.config.UsuarioMapper;
import br.ifsp.consulta_facil_api.dto.UsuarioDTO;
import br.ifsp.consulta_facil_api.model.Role;
import br.ifsp.consulta_facil_api.model.Usuario;
import br.ifsp.consulta_facil_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioMapper usuarioMapper;
    
    


    public Page<UsuarioDTO> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::toDto);
    }

    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioDTO salvar(UsuarioDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);

        if (dto.getSenha() != null) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        // Se nenhum papel for fornecido, definir como PACIENTE por padrão
        if (usuario.getRole() == null) {
            usuario.setRole(Role.PACIENTE);
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(salvo);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Administrador altera o papel de um usuário
    public UsuarioDTO atualizarPapel(Long id, Role novoPapel) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setRole(novoPapel);
        Usuario atualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(atualizado);
    }
}
