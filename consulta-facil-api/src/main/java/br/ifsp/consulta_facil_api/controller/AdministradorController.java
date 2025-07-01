package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.AdministradorDTO;
import br.ifsp.consulta_facil_api.service.AdministradorService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("/administradores")
@Tag(name = "Administradores", description = "Endpoints para gerenciamento de administradores do sistema")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @Operation(
        summary = "Listar todos os administradores",
        description = "Retorna uma lista paginada de todos os administradores do sistema. " +
                     "Acesso público - qualquer usuário pode visualizar a lista de administradores."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de administradores retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = AdministradorDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public Page<AdministradorDTO> listarAdministradores(@Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return administradorService.listar(pageable);
    }

    @Operation(
        summary = "Buscar administrador por ID",
        description = "Retorna os dados de um administrador específico pelo seu ID. " +
                     "Acesso público - qualquer usuário pode visualizar dados de administradores."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrador encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = AdministradorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Administrador não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public AdministradorDTO buscarPorId(@Parameter(description = "ID do administrador") @PathVariable Long id) {
        return administradorService.buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado"));
    }

    @Operation(
        summary = "Criar novo administrador",
        description = "Cria um novo administrador no sistema. " +
                     "Acesso público - qualquer pessoa pode criar um perfil de administrador. " +
                     "O sistema automaticamente cria um usuário associado com papel de ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Administrador criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AdministradorDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdministradorDTO criar(@Parameter(description = "Dados do administrador a ser criado") @Valid @RequestBody AdministradorDTO obj) {
        return administradorService.salvar(obj);
    }

    @Operation(
        summary = "Excluir administrador",
        description = "Remove um administrador do sistema. " +
                     "Acesso público - qualquer usuário autenticado pode excluir administradores. " +
                     "Recomenda-se implementar verificação adicional para garantir que apenas administradores possam excluir outros administradores."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Administrador excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Administrador não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@Parameter(description = "ID do administrador a ser excluído") @PathVariable Long id) {
        administradorService.deletar(id);
    }
}