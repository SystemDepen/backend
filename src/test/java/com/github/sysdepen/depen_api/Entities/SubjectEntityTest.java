package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectEntityTest {
    static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        validator = f.getValidator();
    }

    private Subject novoValido() {
        SubjectInmostVisit subIn = new SubjectInmostVisit();
        subIn.setId(1L);
        subIn.setAccomplice("sim");
        subIn.setVictim(false);
        subIn.setPregnancy(true);
        subIn.setTime_pregnancy("4");
        subIn.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subIn.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSubject("teste");
        subject.setId_inmost_visit(subIn);
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        return subject;
    }

    @Test
    void equals_reflexivo() {
        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Subject a = novoValido();
        a.setId(99L);

        Subject a1 = novoValido();
        a1.setCreated_at(created);
        a1.setUpdated_at(updated);

        Subject a2 = novoValido();
        a2.setCreated_at(created);
        a2.setUpdated_at(updated);

        Subject a3 = novoValido();
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
        Subject d1 = novoValido();

        assertNotEquals(d1, null);           // null
        assertNotEquals(d1, "nao-sou-doc");  // outra classe
    }

    @Test
    void testGettersAndSetters() {
        SubjectInmostVisit subIn = new SubjectInmostVisit();
        subIn.setId(1L);
        subIn.setAccomplice("sim");
        subIn.setVictim(false);
        subIn.setPregnancy(true);
        subIn.setTime_pregnancy("4");
        subIn.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subIn.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSubject("teste");
        subject.setId_inmost_visit(subIn);
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        assertEquals(1L, subject.getId());
        assertEquals("teste", subject.getSubject());
        assertEquals(LocalDateTime.parse("2024-06-24T22:32:00"), subject.getCreated_at());
        assertEquals(LocalDateTime.parse("2024-06-24T22:32:00"), subject.getUpdated_at());
    }

    @Test
    void valido_quando_campos_ok() {
        var a = novoValido();
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    void invalido_quando_subject_empty() {
        var a = novoValido();
        a.setSubject("");
        assertFalse(validator.validate(a).isEmpty());
    }

}
