package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.PacienteDTO;
import br.ifsp.consulta_facil_api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public Page<PacienteDTO> listarTodas(Pageable pageable) {
        return pacienteService.listar(pageable);
    }

    @GetMapping("/{id}")
    PacienteDTO buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    @PostMapping
    public PacienteDTO criar(@RequestBody PacienteDTO obj) {
        return pacienteService.salvar(obj);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
    }
}
