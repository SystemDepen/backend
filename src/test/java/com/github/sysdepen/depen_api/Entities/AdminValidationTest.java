package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AdminValidationTest {

    static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        validator = f.getValidator();
    }

    private Admin novoValido() {
        Admin a = new Admin();
        a.setName("Ana Admin");
        a.setDocument("123.456.789-00"); // casa com o @Pattern
        a.setEmail("ana@exemplo.com");
        a.setPassword("Senha@123");
        a.setRole((short) 1); // ADMIN=1, p.ex.
        a.setCreated_at(java.time.LocalDateTime.now());
        a.setUpdated_at(java.time.LocalDateTime.now());
        return a;
    }

    @Test
    void testGetNameAndPassword() {
        var a = novoValido();
        a.setName("Ana Admin");
        a.setPassword("123456");

        assertEquals("Ana Admin", a.getName());
        assertEquals("123456", a.getPassword());
    }

    @Test
    void equals_reflexivo() {
        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Admin a = novoValido();
        a.setId(99L);

        Admin a1 = novoValido();
        a1.setCreated_at(created);
        a1.setUpdated_at(updated);

        Admin a2 = novoValido();
        a2.setCreated_at(created);
        a2.setUpdated_at(updated);

        Admin a3 = novoValido();
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
        Admin d1 = novoValido();

        assertNotEquals(d1, null);           // null
        assertNotEquals(d1, "nao-sou-doc");  // outra classe
    }

    @Test
    void valido_quando_campos_ok() {
        var a = novoValido();
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    void invalido_quando_name_em_branco() {
        var a = novoValido();
        a.setName(" ");
        assertFalse(validator.validate(a).isEmpty());
    }

    @Test
    void invalido_quando_email_invalido() {
        var a = novoValido();
        a.setEmail("nao-e-email");
        assertFalse(validator.validate(a).isEmpty());
    }

    @Test
    void invalido_quando_email_em_branco() {
        var a = novoValido();
        a.setEmail(" ");
        assertFalse(validator.validate(a).isEmpty());
    }

    @Test
    void invalido_quando_password_em_branco() {
        var a = novoValido();
        a.setPassword(" ");
        assertFalse(validator.validate(a).isEmpty());
    }

    @Test
    void document_valido_11_digitos_sem_pontuacao() {
        var a = novoValido();
        a.setDocument("12345678900");
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    void document_valido_rne_ate_12_alfa_num() {
        var a = novoValido();
        a.setDocument("RNEA12345");
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    void document_invalido_formato_errado() {
        var a = novoValido();
        a.setDocument("1234567890123"); // 13 chars, fora do [a-zA-Z0-9]{1,12}
        assertFalse(validator.validate(a).isEmpty());
    }
}
