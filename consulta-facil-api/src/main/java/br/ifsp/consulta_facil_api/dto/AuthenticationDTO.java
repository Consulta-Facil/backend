package br.ifsp.consulta_facil_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationDTO {
    @NotBlank(message = "Please, enter your email.")
    private String email;
    @NotBlank(message = "Please, enter your password.")
    private String senha;
}