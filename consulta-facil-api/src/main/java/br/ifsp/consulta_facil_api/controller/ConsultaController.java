package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.ConsultaDTO;
import br.ifsp.consulta_facil_api.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;

@RestController
@RequestMapping("/consultas")
@Tag(name = "Consultas", description = "Endpoints para gerenciamento de consultas médicas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Operation(
        summary = "Listar todas as consultas",
        description = "Retorna uma lista paginada de todas as consultas do sistema. " +
                     "Apenas administradores têm permissão para acessar este endpoint. " +
                     "Útil para relatórios e monitoramento geral."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de consultas retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas administradores"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public Page<ConsultaDTO> listarTodas(@Parameter(description = "Parâmetros de paginação (page, size, sort)") Pageable pageable) {
        return consultaService.listar(pageable);
    }

    @Operation(
        summary = "Buscar consulta por ID",
        description = "Retorna os detalhes de uma consulta específica pelo seu ID. " +
                     "Acesso permitido para administradores, profissionais e pacientes. " +
                     "Cada usuário pode buscar apenas consultas relacionadas ao seu papel."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFISSIONAL', 'PACIENTE')")
    @GetMapping("/{id}")
    public ConsultaDTO buscarPorId(@Parameter(description = "ID da consulta") @PathVariable Long id) {
        return consultaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    @Operation(
        summary = "Agendar nova consulta",
        description = "Cria um novo agendamento de consulta. " +
                     "Apenas pacientes podem criar agendamentos. " +
                     "O sistema valida automaticamente: " +
                     "- Disponibilidade do horário " +
                     "- Conflitos com outras consultas " +
                     "- Se o horário está no futuro " +
                     "- Se o profissional existe"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou horário indisponível"),
        @ApiResponse(responseCode = "403", description = "Apenas pacientes podem agendar consultas"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('PACIENTE')")
    @PostMapping
    public ConsultaDTO criar(@Parameter(description = "Dados da consulta a ser agendada") @RequestBody @Valid ConsultaDTO consulta) {
        return consultaService.salvar(consulta);
    }

    @Operation(
        summary = "Cancelar consulta",
        description = "Cancela uma consulta agendada. " +
                     "Apenas pacientes podem cancelar suas próprias consultas. " +
                     "Cancelamentos só são permitidos com pelo menos 24 horas de antecedência. " +
                     "Após o cancelamento, o horário volta a ficar disponível."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Consulta cancelada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Cancelamento sem antecedência de 24h"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
        @ApiResponse(responseCode = "403", description = "Apenas pacientes podem cancelar consultas"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('PACIENTE')")
    @DeleteMapping("/{id}")
    public void deletar(@Parameter(description = "ID da consulta a ser cancelada") @PathVariable Long id) {
        consultaService.deletar(id);
    }

    @Operation(
        summary = "Histórico de consultas do paciente",
        description = "Retorna o histórico de consultas de um paciente específico. " +
                     "Apenas pacientes podem acessar seu próprio histórico. " +
                     "Retorna apenas consultas passadas (concluídas ou canceladas)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/paciente/{id}/historico")
    public Page<ConsultaDTO> historicoPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long id, 
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return consultaService.listarHistoricoDoPaciente(id, pageable);
    }

    @Operation(
        summary = "Consultas do profissional por data",
        description = "Retorna todas as consultas de um profissional em uma data específica. " +
                     "Apenas profissionais podem acessar suas próprias consultas. " +
                     "Útil para visualizar agenda diária de atendimentos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas do dia retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @GetMapping("/profissional/{id}/dia")
    public Page<ConsultaDTO> listarPorData(
            @Parameter(description = "ID do profissional") @PathVariable Long id,
            @Parameter(description = "Data das consultas (formato: YYYY-MM-DD)") @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return consultaService.listarPorData(id, data, pageable);
    }

    @Operation(
        summary = "Consultas do profissional com paciente específico",
        description = "Retorna todas as consultas entre um profissional e um paciente específico. " +
                     "Apenas profissionais podem acessar suas próprias consultas. " +
                     "Útil para acompanhar histórico de atendimento de um paciente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @GetMapping("/profissional/{id}/paciente/{idPaciente}")
    public Page<ConsultaDTO> listarPorPaciente(
            @Parameter(description = "ID do profissional") @PathVariable Long id,
            @Parameter(description = "ID do paciente") @PathVariable Long idPaciente,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return consultaService.listarPorPaciente(id, idPaciente, pageable);
    }

    @Operation(
        summary = "Consultas por data (Administrador)",
        description = "Retorna todas as consultas do sistema em uma data específica. " +
                     "Apenas administradores podem acessar este endpoint. " +
                     "Útil para relatórios diários e monitoramento de atendimentos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas da data retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas administradores"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/data")
    public Page<ConsultaDTO> consultasPorData(
            @Parameter(description = "Data das consultas (formato: YYYY-MM-DD)") @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return consultaService.listarConsultasPorData(data, pageable);
    }

    @Operation(
        summary = "Consultas por profissional (Administrador)",
        description = "Retorna todas as consultas de um profissional específico. " +
                     "Apenas administradores podem acessar este endpoint. " +
                     "Útil para relatórios de produtividade e acompanhamento de profissionais."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas do profissional retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas administradores"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/profissional/{id}")
    public Page<ConsultaDTO> consultasPorProfissional(
            @Parameter(description = "ID do profissional") @PathVariable Long id, 
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        return consultaService.listarConsultasPorProfissional(id, pageable);
    }
}
