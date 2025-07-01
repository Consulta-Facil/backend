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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários do sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Operation(
        summary = "Registrar novo usuário",
        description = "Cria um novo usuário no sistema. " +
                     "Acesso público - qualquer pessoa pode se registrar. " +
                     "Por padrão, novos usuários são criados com papel de PACIENTE. " +
                     "A senha é criptografada automaticamente antes de ser salva."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registrar(@Parameter(description = "Dados do usuário a ser registrado") @RequestBody @Valid UsuarioDTO dto) {
        UsuarioDTO salvo = usuarioService.salvar(dto);
        salvo.setSenha(null); // Segurança: não retorna senha
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista paginada de todos os usuários do sistema. " +
                     "Apenas administradores podem acessar este endpoint. " +
                     "As senhas não são retornadas por questões de segurança."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas administradores"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listarTodos(@Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(pageable));
    }

    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os dados de um usuário específico pelo seu ID. " +
                     "Acesso público - qualquer usuário autenticado pode buscar dados de outros usuários. " +
                     "A senha não é retornada por questões de segurança."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@Parameter(description = "ID do usuário") @PathVariable Long id) {
        Optional<UsuarioDTO> usuario = usuarioService.buscarPorId(id);
        usuario.ifPresent(u -> u.setSenha(null)); // Segurança
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Excluir usuário",
        description = "Remove um usuário do sistema. " +
                     "Acesso público - qualquer usuário autenticado pode excluir outros usuários. " +
                     "Recomenda-se implementar verificação adicional para garantir que apenas o próprio usuário ou administradores possam excluir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Parameter(description = "ID do usuário a ser excluído") @PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obter dados do usuário logado",
        description = "Retorna os dados do usuário atualmente autenticado. " +
                     "Apenas usuários autenticados podem acessar seus próprios dados. " +
                     "A senha não é retornada por questões de segurança."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do usuário retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado(@Parameter(description = "Usuário autenticado (injetado automaticamente)") @AuthenticationPrincipal UsuarioAutenticado userAuth) {
        UsuarioDTO dto = usuarioMapper.toDto(userAuth.getUsuario());
        dto.setSenha(null); // Não expõe a senha
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "Atualizar papel do usuário",
        description = "Altera o papel (role) de um usuário no sistema. " +
                     "Apenas administradores podem alterar papéis de outros usuários. " +
                     "Papéis disponíveis: PACIENTE, PROFISSIONAL, ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Papel atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "403", description = "Apenas administradores podem alterar papéis"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}/papel")
    public ResponseEntity<UsuarioDTO> atualizarPapel(
            @Parameter(description = "ID do usuário") @PathVariable Long id, 
            @Parameter(description = "Novo papel do usuário (PACIENTE, PROFISSIONAL, ADMINISTRADOR)") @RequestParam Role role) {
        UsuarioDTO atualizado = usuarioService.atualizarPapel(id, role);
        atualizado.setSenha(null);
        return ResponseEntity.ok(atualizado);
    }
}
