package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.HorarioDTO;
import br.ifsp.consulta_facil_api.service.HorarioService;

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
@RequestMapping("/horarios")
@Tag(name = "Horários", description = "Endpoints para gerenciamento de horários de atendimento")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @Operation(
        summary = "Listar todos os horários",
        description = "Retorna uma lista paginada de todos os horários de atendimento do sistema. " +
                     "Acesso público - qualquer usuário pode visualizar os horários disponíveis."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de horários retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = HorarioDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public Page<HorarioDTO> listarTodas(@Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return horarioService.listar(pageable);
    }

    @Operation(
        summary = "Buscar horário por ID",
        description = "Retorna os detalhes de um horário específico pelo seu ID. " +
                     "Acesso público - qualquer usuário pode buscar informações de um horário."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horário encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = HorarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Horário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public HorarioDTO buscarPorId(@Parameter(description = "ID do horário") @PathVariable Long id) {
        return horarioService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
    }

    @Operation(
        summary = "Criar novo horário de atendimento",
        description = "Cria um novo horário de atendimento. " +
                     "Apenas profissionais podem criar horários. " +
                     "O sistema valida automaticamente: " +
                     "- Se o horário está no futuro " +
                     "- Se não há conflitos com outros horários do profissional " +
                     "- Se o profissional existe"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Horário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = HorarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou conflito de horário"),
        @ApiResponse(responseCode = "403", description = "Apenas profissionais podem criar horários"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @PostMapping
    public HorarioDTO criar(@Parameter(description = "Dados do horário a ser criado") @RequestBody @Valid HorarioDTO obj) {
        return horarioService.salvar(obj);
    }

    @Operation(
        summary = "Excluir horário de atendimento",
        description = "Remove um horário de atendimento do sistema. " +
                     "Apenas profissionais podem excluir seus próprios horários. " +
                     "Horários com consultas agendadas não podem ser excluídos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Horário excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Horário possui consultas agendadas"),
        @ApiResponse(responseCode = "404", description = "Horário não encontrado"),
        @ApiResponse(responseCode = "403", description = "Apenas profissionais podem excluir horários"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @DeleteMapping("/{id}")
    public void deletar(@Parameter(description = "ID do horário a ser excluído") @PathVariable Long id) {
        horarioService.deletar(id);
    }

    @Operation(
        summary = "Horários de um profissional",
        description = "Retorna todos os horários de atendimento de um profissional específico. " +
                     "Acesso público - qualquer usuário pode visualizar os horários de um profissional."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horários do profissional retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = HorarioDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/profissional/{id}")
    public Page<HorarioDTO> listarPorProfissional(
            @Parameter(description = "ID do profissional") @PathVariable Long id, 
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return horarioService.listarPorProfissional(id, pageable);
    }

    @Operation(
        summary = "Horários disponíveis de um profissional",
        description = "Retorna apenas os horários disponíveis (sem consultas agendadas) de um profissional. " +
                     "Acesso público - útil para pacientes escolherem horários para agendamento."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horários disponíveis retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = HorarioDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/profissionais/{id}/horarios")
    public Page<HorarioDTO> listarHorarios(
            @Parameter(description = "ID do profissional") @PathVariable Long id, 
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return horarioService.listarHorariosDisponiveis(id, pageable);
    }
}
