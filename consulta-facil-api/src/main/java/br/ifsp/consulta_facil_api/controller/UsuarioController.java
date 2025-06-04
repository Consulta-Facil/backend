package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.config.UsuarioMapper;
import br.ifsp.consulta_facil_api.dto.UsuarioDTO;
import br.ifsp.consulta_facil_api.model.Role;
import br.ifsp.consulta_facil_api.model.UsuarioAutenticado;
import br.ifsp.consulta_facil_api.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Rota pública - cadastro inicial (default: PACIENTE)
    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        UsuarioDTO salvo = usuarioService.salvar(dto);
        salvo.setSenha(null); // Segurança: não retorna senha
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    // Rota protegida - acesso restrito a ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(pageable));
    }

    // Rota protegida - ADMIN ou dono do recurso (idealmente com verificação adicional)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuario = usuarioService.buscarPorId(id);
        usuario.ifPresent(u -> u.setSenha(null)); // Segurança
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // Rota protegida - ADMIN ou dono (verificação adicional recomendada)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Rota protegida - usuário autenticado acessa seus próprios dados
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado(@AuthenticationPrincipal UsuarioAutenticado userAuth) {
        UsuarioDTO dto = usuarioMapper.toDto(userAuth.getUsuario());
        dto.setSenha(null); // Não expõe a senha
        return ResponseEntity.ok(dto);
    }

    // Rota protegida - apenas ADMIN pode mudar papel de outro usuário
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}/papel")
    public ResponseEntity<UsuarioDTO> atualizarPapel(@PathVariable Long id, @RequestParam Role role) {
        UsuarioDTO atualizado = usuarioService.atualizarPapel(id, role);
        atualizado.setSenha(null);
        return ResponseEntity.ok(atualizado);
    }
}
