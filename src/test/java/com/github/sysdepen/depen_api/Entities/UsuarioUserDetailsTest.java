package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioUserDetailsTest {

    private Usuario base() {
        Usuario u = new Usuario();
        u.setName("Ana");
        u.setDocument("12345678900");
        u.setPassword("senha");
        u.setEmail("ana@exemplo.com");
        return u;
    }

    @Test
    void username_e_document_e_password_ok() {
        var u = base();
        assertEquals("12345678900", u.getUsername());
        assertEquals("senha", u.getPassword());
    }

    @Test
    void flags_de_conta_true() {
        var u = base();
        assertTrue(u.isAccountNonExpired());
        assertTrue(u.isAccountNonLocked());
        assertTrue(u.isCredentialsNonExpired());
        assertTrue(u.isEnabled());
    }

    @Test
    void authorities_com_role() {
        var u = base();
        u.setRole("ROLE_USER");
        Collection<? extends GrantedAuthority> auths = u.getAuthorities();
        assertEquals(1, auths.size());
        assertTrue(auths.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void authorities_sem_role_lanca_iae() {
        var u = base();
        u.setRole(null);
        assertThrows(IllegalArgumentException.class, u::getAuthorities);
    }

    @Test
    void authorities_role_em_branco_lanca_iae() {
        var u = base();
        u.setRole("  ");
        assertThrows(IllegalArgumentException.class, u::getAuthorities);
    }
}