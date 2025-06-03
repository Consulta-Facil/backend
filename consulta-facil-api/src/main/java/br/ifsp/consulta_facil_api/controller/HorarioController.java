package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.HorarioDTO;
import br.ifsp.consulta_facil_api.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    HorarioDTO buscarPorId(@PathVariable Long id) {
        return horarioService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Horario não encontrado"));
    }

    @PostMapping
    public HorarioDTO criar(@RequestBody HorarioDTO obj) {
        return horarioService.salvar(obj);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        horarioService.deletar(id);
    }
}
