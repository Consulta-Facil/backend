package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.ConsultaDTO;
import br.ifsp.consulta_facil_api.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Temporariamente comentado devido a conflito de versão
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/consultas")
// @Tag(name = "Consultas", description = "Endpoints para gerenciamento de consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // @Operation(summary = "Listar todas as consultas", description = "Apenas administradores podem listar todas as consultas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public Page<ConsultaDTO> listarTodas(/* @Parameter(description = "Parâmetros de paginação") */ Pageable pageable) {
        return consultaService.listar(pageable);
    }

    // 🔐 Acesso geral (ADMIN, PROFISSIONAL, PACIENTE) para buscar uma consulta específica
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFISSIONAL', 'PACIENTE')")
    @GetMapping("/{id}")
    public ConsultaDTO buscarPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    // 🔐 Apenas PACIENTE pode criar agendamento
    @PreAuthorize("hasRole('PACIENTE')")
    @PostMapping
    public ConsultaDTO criar(@RequestBody @Valid ConsultaDTO consulta) {
        return consultaService.salvar(consulta);
    }

    // 🔐 Apenas PACIENTE pode cancelar (com 24h de antecedência)
    @PreAuthorize("hasRole('PACIENTE')")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        consultaService.deletar(id);
    }

    // 🔐 Histórico do paciente logado
    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/paciente/{id}/historico")
    public Page<ConsultaDTO> historicoPaciente(@PathVariable Long id, Pageable pageable) {
        return consultaService.listarHistoricoDoPaciente(id, pageable);
    }

    // 🔐 Consultas do profissional em uma data específica
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @GetMapping("/profissional/{id}/dia")
    public Page<ConsultaDTO> listarPorData(
            @PathVariable Long id,
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            Pageable pageable) {
        return consultaService.listarPorData(id, data, pageable);
    }

    // 🔐 Consultas do profissional com determinado paciente
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @GetMapping("/profissional/{id}/paciente/{idPaciente}")
    public Page<ConsultaDTO> listarPorPaciente(
            @PathVariable Long id,
            @PathVariable Long idPaciente,
            Pageable pageable) {
        return consultaService.listarPorPaciente(id, idPaciente, pageable);
    }

    // 🔐 Consultas por data (admin)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/data")
    public Page<ConsultaDTO> consultasPorData(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            Pageable pageable) {
        return consultaService.listarConsultasPorData(data, pageable);
    }

    // 🔐 Consultas por profissional (admin)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/profissional/{id}")
    public Page<ConsultaDTO> consultasPorProfissional(@PathVariable Long id, Pageable pageable) {
        return consultaService.listarConsultasPorProfissional(id, pageable);
    }
}
