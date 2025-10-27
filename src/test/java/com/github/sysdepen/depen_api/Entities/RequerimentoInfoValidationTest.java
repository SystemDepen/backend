package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.entity.RequerimentoInfo;
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

public class RequerimentoInfoValidationTest {

    static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        validator = f.getValidator();
    }

    private RequerimentoInfo novoValido() {
        RequerimentoInfo req = new RequerimentoInfo();
        Subject subject = new Subject();
        SubjectInmostVisit subIn = new SubjectInmostVisit();

        subIn.setId(1L);
        subIn.setAccomplice("sim");
        subIn.setVictim(false);
        subIn.setPregnancy(true);
        subIn.setTime_pregnancy("4");
        subIn.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subIn.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        subject.setId(1L);
        subject.setSubject("teste");
        subject.setId_inmost_visit(subIn);
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        req.setId(1L);
        req.setName_visited("test");
        req.setCpf_rne("123456789002");
        req.setType_visitation("Teste");
        req.setCellphone("45988888888");
        req.setState("PR");
        req.setCity("medianeira");
        req.setDistrict("belo");
        req.setStreet("teste");
        req.setNumber_house("444a");
        req.setSubject(subject);

        return req;
    }

    @Test
    void equals_reflexivo() {
        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Subject subject = new Subject();
        subject.setId(1L);

        RequerimentoInfo a = novoValido();
        a.setId(99L);
        a.setSubject(subject);

        RequerimentoInfo a1 = novoValido();
        a1.setSubject(subject);

        RequerimentoInfo a2 = novoValido();
        a2.setSubject(subject);

        RequerimentoInfo a3 = novoValido();
        a3.setSubject(subject);

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
        RequerimentoInfo d1 = novoValido();

        assertNotEquals(d1, null);           // null
        assertNotEquals(d1, "nao-sou-requerimento");  // outra classe
    }

    @Test
    void testGettersAndSetters() {
        RequerimentoInfo req = new RequerimentoInfo();
        Subject subject = new Subject();
        SubjectInmostVisit subIn = new SubjectInmostVisit();

        subIn.setId(1L);
        subIn.setAccomplice("sim");
        subIn.setVictim(false);
        subIn.setPregnancy(true);
        subIn.setTime_pregnancy("4");
        subIn.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subIn.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        subject.setId(1L);
        subject.setSubject("teste");
        subject.setId_inmost_visit(subIn);
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        req.setId(1L);
        req.setName_visited("test");
        req.setCpf_rne("123456789002");
        req.setType_visitation("Teste");
        req.setCellphone("45988888888");
        req.setState("PR");
        req.setCity("medianeira");
        req.setDistrict("belo");
        req.setStreet("teste");
        req.setNumber_house("444a");
        req.setSubject(subject);

        assertEquals(1L, req.getId());
        assertEquals("test", req.getName_visited());
        assertEquals("45988888888", req.getCellphone());
        assertEquals("PR", req.getState());
        assertEquals("medianeira", req.getCity());
        assertEquals("belo", req.getDistrict());
        assertEquals("teste", req.getStreet());
        assertEquals("444a", req.getNumber_house());

    }

    @Test
    void invalido_quando_telefone_em_branco() {
        var u = novoValido();
        u.setCellphone(" ");
        assertFalse(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_UF_em_branco() {
        var u = novoValido();
        u.setState(" ");
        assertFalse(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_Street_em_branco() {
        var u = novoValido();
        u.setStreet(" ");
        assertFalse(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_nome_visitado_em_branco() {
        var u = novoValido();
        u.setName_visited(" ");
        assertFalse(validator.validate(u).isEmpty());
    }

    @Test
    void invalido_quando_numero_em_branco() {
        var u = novoValido();
        u.setNumber_house(" ");
        assertFalse(validator.validate(u).isEmpty());
    }
}
