package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.ConsultaDTO;
import br.ifsp.consulta_facil_api.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public Page<ConsultaDTO> listarTodas(Pageable pageable) {
        return consultaService.listar(pageable);
    }

    @GetMapping("/{id}")
    public ConsultaDTO buscarPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    @PostMapping
    public ConsultaDTO criar(@RequestBody ConsultaDTO consulta) {
        return consultaService.salvar(consulta);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        consultaService.deletar(id);
    }
}