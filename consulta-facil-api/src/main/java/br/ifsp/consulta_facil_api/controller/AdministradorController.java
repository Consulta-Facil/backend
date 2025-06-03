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


@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public Page<AdministradorDTO> listarAdministradores(Pageable pageable) {
        return administradorService.listar(pageable);
    }

    @GetMapping("/{id}")
    public AdministradorDTO buscarPorId(@PathVariable Long id) {
        return administradorService.buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdministradorDTO criar(@Valid @RequestBody AdministradorDTO obj) {
        return administradorService.salvar(obj);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        administradorService.deletar(id);
    }
}