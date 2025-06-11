package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.HorarioDTO;
import br.ifsp.consulta_facil_api.service.HorarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping
    public Page<HorarioDTO> listarTodas(Pageable pageable) {
        return horarioService.listar(pageable);
    }

    @GetMapping("/{id}")
    public HorarioDTO buscarPorId(@PathVariable Long id) {
        return horarioService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
    }

    @PreAuthorize("hasRole('PROFISSIONAL')")
    @PostMapping
    public HorarioDTO criar(@RequestBody HorarioDTO obj) {
        return horarioService.salvar(obj);
    }

    @PreAuthorize("hasRole('PROFISSIONAL')")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        horarioService.deletar(id);
    }

    @GetMapping("/profissional/{id}")
    public Page<HorarioDTO> listarPorProfissional(@PathVariable Long id, Pageable pageable) {
        return horarioService.listarPorProfissional(id, pageable);
    }

    @GetMapping("/profissionais/{id}/horarios")
    public Page<HorarioDTO> listarHorarios(@PathVariable Long id, Pageable pageable) {
        return horarioService.listarHorariosDisponiveis(id, pageable);
    }
}
