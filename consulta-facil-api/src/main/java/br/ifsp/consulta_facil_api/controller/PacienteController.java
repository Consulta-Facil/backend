package br.ifsp.consulta_facil_api.controller;

import br.ifsp.consulta_facil_api.dto.PacienteDTO;
import br.ifsp.consulta_facil_api.model.UsuarioAutenticado;
import br.ifsp.consulta_facil_api.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;
    
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public Page<PacienteDTO> listarTodas(Pageable pageable) {
        return pacienteService.listar(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {

        if (!usuarioAutenticado.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return pacienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PacienteDTO criar(@RequestBody PacienteDTO obj) {
        return pacienteService.salvar(obj);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
    }

    
    @PutMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<PacienteDTO> atualizarDados(
            @RequestBody PacienteDTO dto,
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {

        try {
            PacienteDTO atualizado = pacienteService.atualizarDados(usuarioAutenticado.getId(), dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
