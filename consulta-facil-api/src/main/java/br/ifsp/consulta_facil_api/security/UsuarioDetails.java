package br.ifsp.consulta_facil_api.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.ifsp.consulta_facil_api.model.Usuario;

public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // ou lógica própria
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // ou lógica própria
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // ou lógica própria
    }

    @Override
    public boolean isEnabled() {
        return true; // ou lógica própria
    }

    // getter para o usuario original se quiser
    public Usuario getUsuario() {
        return usuario;
    }
}
