package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.github.sysdepen.depen_api.entity.Protocols;

import java.util.List;

class UsuarioEntityTest {

    @Test
    void testGettersAndSetters() {
        Usuario usuario = new Usuario();

        LocalDateTime now = LocalDateTime.now();

        usuario.setId(1L);
        usuario.setName("João da Silva");
        usuario.setDocument("12345678900");
        usuario.setPassword("senha123");
        usuario.setEmail("joao@email.com");
        usuario.setRole("ROLE_USER");
        usuario.setDate_born(now.minusYears(20));
        usuario.setCreated_at(now);
        usuario.setUpdated_at(now);

        assertEquals(1L, usuario.getId());
        assertEquals("João da Silva", usuario.getName());
        assertEquals("12345678900", usuario.getDocument());
        assertEquals("senha123", usuario.getPassword());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("ROLE_USER", usuario.getRole());
        assertEquals(now.minusYears(20), usuario.getDate_born());
        assertEquals(now, usuario.getCreated_at());
        assertEquals(now, usuario.getUpdated_at());
    }

    @Test
    void testAuthoritiesReturnsCorrectRole() {
        Usuario usuario = new Usuario();
        usuario.setRole("ROLE_ADMIN");

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void testUserDetailsMethodsReturnTrue() {
        Usuario usuario = new Usuario();

        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }

    @Test
    void testGetUsernameAndPassword() {
        Usuario usuario = new Usuario();
        usuario.setDocument("99999999999");
        usuario.setPassword("minhaSenha");

        assertEquals("99999999999", usuario.getUsername());
        assertEquals("minhaSenha", usuario.getPassword());
    }

    @Test
    void testProtocolsList() {
        Usuario usuario = new Usuario();
        Protocols protocolo = new Protocols();
        protocolo.setUser(usuario);

        usuario.setProtocols(List.of(protocolo));

        assertEquals(1, usuario.getProtocols().size());
        assertEquals(usuario, usuario.getProtocols().get(0).getUser());
    }
}
