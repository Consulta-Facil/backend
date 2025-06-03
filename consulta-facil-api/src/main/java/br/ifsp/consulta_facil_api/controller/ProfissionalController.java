package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.ProfissionalDTO;
import br.ifsp.consulta_facil_api.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @GetMapping
    public Page<ProfissionalDTO> listarTodas(Pageable pageable) {
        return profissionalService.listar(pageable);
    }

    @GetMapping("/{id}")
    ProfissionalDTO buscarPorId(@PathVariable Long id) {
        return profissionalService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    }

    @PostMapping
    public ProfissionalDTO criar(@RequestBody ProfissionalDTO obj) {
        return profissionalService.salvar(obj);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        profissionalService.deletar(id);
    }
}
