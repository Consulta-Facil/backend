package br.ifsp.consulta_facil_api.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;


import br.ifsp.consulta_facil_api.model.Usuario;
import br.ifsp.consulta_facil_api.model.UsuarioAutenticado;
import br.ifsp.consulta_facil_api.repository.UsuarioRepository;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomJwtAuthenticationConverter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UsuarioAutenticado userAuthenticated = extractUser(jwt);
        List<GrantedAuthority> authorities = List.copyOf(userAuthenticated.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userAuthenticated, null, authorities);
    }

    private UsuarioAutenticado extractUser(Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        return new UsuarioAutenticado(user);
    }
}
