package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.ProfissionalDTO;
import br.ifsp.consulta_facil_api.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/profissionais")
@Tag(name = "Profissionais", description = "Endpoints para gerenciamento de dados dos profissionais de saúde")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @Operation(
        summary = "Listar todos os profissionais",
        description = "Retorna uma lista paginada de todos os profissionais de saúde do sistema. " +
                     "Apenas administradores podem acessar este endpoint. " +
                     "Útil para gestão de profissionais e relatórios."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de profissionais retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfissionalDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas administradores"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Page<ProfissionalDTO> listarProfissionais(@Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return profissionalService.listar(pageable);
    }

    @Operation(
        summary = "Buscar profissional por ID",
        description = "Retorna os dados de um profissional específico pelo seu ID. " +
                     "Acesso público - qualquer usuário pode visualizar dados de profissionais."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profissional encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfissionalDTO.class))),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    ProfissionalDTO buscarPorId(@Parameter(description = "ID do profissional") @PathVariable Long id) {
        return profissionalService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    }

    
    @Operation(
        summary = "Criar novo profissional",
        description = "Cria um novo profissional de saúde no sistema. " +
                     "Acesso público - qualquer pessoa pode criar um perfil de profissional. " +
                     "O sistema automaticamente cria um usuário associado com papel de PROFISSIONAL."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profissional criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfissionalDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ProfissionalDTO criar(@Parameter(description = "Dados do profissional a ser criado") @RequestBody @Valid ProfissionalDTO obj) {
        return profissionalService.salvar(obj);
    }
    
    @Operation(
        summary = "Atualizar dados do profissional",
        description = "Atualiza os dados de um profissional específico. " +
                     "Acesso público - qualquer usuário autenticado pode atualizar dados de profissionais. " +
                     "Recomenda-se implementar verificação adicional para garantir que apenas o próprio profissional ou administradores possam atualizar."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profissional atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfissionalDTO.class))),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PutMapping("/{id}")
    public ProfissionalDTO atualizarProfissional(
            @Parameter(description = "ID do profissional") @PathVariable Long id, 
            @Parameter(description = "Novos dados do profissional") @RequestBody @Valid ProfissionalDTO dto) {
        return profissionalService.atualizarProfissional(id, dto);
    }

    
    @Operation(
        summary = "Excluir profissional",
        description = "Remove um profissional do sistema. " +
                     "Apenas administradores podem excluir profissionais. " +
                     "Profissionais com consultas agendadas não podem ser excluídos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Profissional excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Profissional possui consultas agendadas"),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado"),
        @ApiResponse(responseCode = "403", description = "Apenas administradores podem excluir profissionais"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void deletar(@Parameter(description = "ID do profissional a ser excluído") @PathVariable Long id) {
        profissionalService.deletar(id);
    }
}
