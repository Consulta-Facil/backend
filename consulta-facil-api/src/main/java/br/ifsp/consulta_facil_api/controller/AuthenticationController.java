package br.ifsp.consulta_facil_api.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.consulta_facil_api.dto.AuthenticationDTO;
import br.ifsp.consulta_facil_api.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e geração de tokens JWT")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
        summary = "Autenticar usuário",
        description = "Autentica um usuário com email e senha, retornando um token JWT válido. " +
                     "O token deve ser incluído no header Authorization para acessar endpoints protegidos. " +
                     "Formato: Bearer {token}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(schema = @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    @PostMapping("authenticate")
    public String authenticate(@Parameter(description = "Credenciais de autenticação (email e senha)") @RequestBody @Valid AuthenticationDTO request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getSenha());
        return authenticationService.authenticate(authentication);
    }
}
