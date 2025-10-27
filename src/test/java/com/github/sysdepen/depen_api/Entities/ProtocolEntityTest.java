package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtocolEntityTest {

    static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        validator = f.getValidator();
    }

    private Protocols novoValido() {
        Protocols protocols = new Protocols();
        Usuario usr = new Usuario();

        usr.setId(1L);
        usr.setName("Test");
        usr.setDocument("123456789002");
        usr.setEmail("kwanza@email.com");
        usr.setPassword("123456");
        usr.setRole("default");
        usr.setDate_born(LocalDateTime.parse("2024-06-24T22:32:00"));
        usr.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        usr.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));


        protocols.setId(1L);
        protocols.setUser(usr);
        protocols.setDoc(null);
        protocols.setAdmin(null);
        protocols.setStatus(1L);
        protocols.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        protocols.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        return protocols;
    }

    @Test
    void equals_reflexivo() {
        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Usuario usr = new Usuario();
        usr.setId(1L);

        Protocols a = novoValido();
        a.setId(99L);
        a.setUser(usr);

        Protocols a1 = novoValido();
        a1.setUser(usr);
        a1.setCreated_at(created);
        a1.setUpdated_at(updated);

        Protocols a2 = novoValido();
        a2.setUser(usr);
        a2.setCreated_at(created);
        a2.setUpdated_at(updated);

        Protocols a3 = novoValido();
        a3.setUser(usr);
        a3.setCreated_at(created);
        a3.setUpdated_at(updated);

        // Reflexivo
        assertEquals(a1, a1);

        // Simétrico
        assertEquals(a1, a2);
        assertEquals(a2, a1);

        // Transitivo
        assertEquals(a2, a3);
        assertEquals(a1, a3);

        // hashCode consistente com equals
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void equals_com_null_e_com_outra_classe_retorna_false() {
        Protocols d1 = novoValido();

        assertNotEquals(d1, null);           // null
        assertNotEquals(d1, "nao-sou-protocol");  // outra classe
    }

    @Test
    void testGettersAndSetters() {
        Protocols protocols = new Protocols();
        Usuario usr = new Usuario();

        usr.setId(1L);
        usr.setName("Test");
        usr.setDocument("123456789002");
        usr.setEmail("kwanza@email.com");
        usr.setPassword("123456");
        usr.setRole("default");
        usr.setDate_born(LocalDateTime.parse("2024-06-24T22:32:00"));
        usr.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        usr.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));


        protocols.setId(1L);
        protocols.setUser(usr);
        protocols.setDoc(null);
        protocols.setAdmin(null);
        protocols.setStatus(1L);
        protocols.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        protocols.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        assertEquals(1L, protocols.getId());
        assertEquals(1L, protocols.getStatus());
    }

    @Test
    void valido_quando_campos_ok() {
        var a = novoValido();
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    void invalido_quando_user_null() {
        var a = novoValido();
        a.setStatus(null);
        assertFalse(validator.validate(a).isEmpty());
    }
}
