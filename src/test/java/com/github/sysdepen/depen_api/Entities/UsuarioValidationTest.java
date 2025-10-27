package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.security.auth.Usuario;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioValidationTest {
    static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Usuario novoValido() {
        Usuario u = new Usuario();
        u.setName("Ana");
        u.setDocument("12345678900");
        u.setPassword("senhaSegura");
        u.setEmail("ana@exemplo.com");
        u.setRole("ROLE_USER");
        return u;
    }

    @Test
    void valido_quando_campos_ok() {
        var u = novoValido();
        assertTrue(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_nome_em_branco() {
        var u = novoValido();
        u.setName(" ");
        assertFalse(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_documento_em_branco() {
        var u = novoValido();
        u.setDocument(" ");
        assertFalse(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_senha_em_branco() {
        var u = novoValido();
        u.setPassword(" ");
        assertFalse(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_email_invalido() {
        var u = novoValido();
        u.setEmail("nao-e-email");
        assertFalse(validator.validate(u).isEmpty());
    }
}