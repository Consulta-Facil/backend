package br.ifsp.consulta_facil_api.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.ifsp.consulta_facil_api.model.Usuario;
import br.ifsp.consulta_facil_api.repository.UsuarioRepository;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    
    public AuthenticationService(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }
    
    public String authenticate(Authentication authentication) {
        String email = authentication.getName();     
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtService.generateToken(usuario);
    }
}