package br.ifsp.consulta_facil_api.service;

import java.time.Instant;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.ifsp.consulta_facil_api.model.Usuario;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;
    
    public JwtService(JwtEncoder encoder) {
        this.jwtEncoder = encoder;
    }
    
    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();
        long expire = 3600L;
    
        var claims = JwtClaimsSet.builder()
                .issuer("spring-security")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expire))
                .subject(usuario.getEmail())
                .claim("userId", usuario.getId())
                .claim("role", usuario.getRole().name())  // <-- adiciona o papel aqui
                .build();

    
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
}
