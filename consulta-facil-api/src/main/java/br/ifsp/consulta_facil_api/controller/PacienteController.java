package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.PacienteDTO;
import br.ifsp.consulta_facil_api.model.UsuarioAutenticado;
import br.ifsp.consulta_facil_api.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para gerenciamento de dados dos pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;
    
    @Operation(
        summary = "Listar todos os pacientes",
        description = "Retorna uma lista paginada de todos os pacientes do sistema. " +
                     "Apenas administradores podem acessar este endpoint. " +
                     "Útil para relatórios e gestão de pacientes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas administradores"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public Page<PacienteDTO> listarTodas(@Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return pacienteService.listar(pageable);
    }

    @Operation(
        summary = "Buscar paciente por ID",
        description = "Retorna os dados de um paciente específico pelo seu ID. " +
                     "Pacientes só podem acessar seus próprios dados. " +
                     "Administradores podem acessar dados de qualquer paciente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado - paciente tentando acessar dados de outro paciente"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(
            @Parameter(description = "ID do paciente") @PathVariable Long id,
            @Parameter(description = "Usuário autenticado (injetado automaticamente)") @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {

        if (!usuarioAutenticado.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return pacienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Criar novo paciente",
        description = "Cria um novo paciente no sistema. " +
                     "Acesso público - qualquer pessoa pode criar um perfil de paciente. " +
                     "O sistema automaticamente cria um usuário associado com papel de PACIENTE."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public PacienteDTO criar(@Parameter(description = "Dados do paciente a ser criado") @RequestBody @Valid PacienteDTO obj) {
        return pacienteService.salvar(obj);
    }

    @Operation(
        summary = "Excluir paciente",
        description = "Remove um paciente do sistema. " +
                     "Acesso público - qualquer usuário autenticado pode excluir pacientes. " +
                     "Recomenda-se implementar verificação adicional para garantir que apenas o próprio paciente ou administradores possam excluir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @DeleteMapping("/{id}")
    public void deletar(@Parameter(description = "ID do paciente a ser excluído") @PathVariable Long id) {
        pacienteService.deletar(id);
    }

    
    @Operation(
        summary = "Atualizar dados do paciente logado",
        description = "Permite que um paciente atualize seus próprios dados. " +
                     "Apenas pacientes podem acessar este endpoint. " +
                     "O sistema automaticamente associa os dados ao paciente logado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
        @ApiResponse(responseCode = "403", description = "Apenas pacientes podem atualizar dados"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PutMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<PacienteDTO> atualizarDados(
            @Parameter(description = "Novos dados do paciente") @RequestBody @Valid PacienteDTO dto,
            @Parameter(description = "Usuário autenticado (injetado automaticamente)") @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {

        try {
            PacienteDTO atualizado = pacienteService.atualizarDados(usuarioAutenticado.getId(), dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
