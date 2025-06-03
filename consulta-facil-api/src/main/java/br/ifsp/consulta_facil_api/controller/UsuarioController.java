package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.config.UsuarioMapper;
import br.ifsp.consulta_facil_api.dto.UsuarioDTO;
import br.ifsp.consulta_facil_api.model.UsuarioAutenticado;
import br.ifsp.consulta_facil_api.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

	@Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper; 

    // rota pública
    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        UsuarioDTO salvo = usuarioService.salvar(dto);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    // rota protegida - por perfil ADMIN, por exemplo
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(pageable));
    }

    // rota protegida
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // rota protegida - pode exigir ADMIN ou dono do recurso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar usuário logado
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado(@AuthenticationPrincipal UsuarioAutenticado userAuth) {
        UsuarioDTO dto = usuarioMapper.toDto(userAuth.getUsuario());
        // remover a senha do DTO, se estiver presente (por segurança)
        dto.setSenha(null);
        return ResponseEntity.ok(dto);
    }
}
