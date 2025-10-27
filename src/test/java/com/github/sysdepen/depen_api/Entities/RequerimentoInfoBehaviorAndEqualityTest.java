package com.github.sysdepen.depen_api.Entities;


import com.github.sysdepen.depen_api.entity.RequerimentoInfo;
import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class RequerimentoInfoBehaviorAndEqualityTest {

    private SubjectInmostVisit makeInmost() {
        SubjectInmostVisit s = new SubjectInmostVisit();
        s.setId(1L);
        s.setAccomplice("sim");
        s.setVictim(false);
        s.setPregnancy(true);
        s.setTime_pregnancy("4");
        s.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        s.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        return s;
    }

    private Subject makeSubject(SubjectInmostVisit inmost) {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSubject("teste");
        subject.setId_inmost_visit(inmost);
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        return subject;
    }

    private RequerimentoInfo makeReq(Subject subject) {
        RequerimentoInfo req = new RequerimentoInfo();
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

    // -----------------------
    // Cobertura de toString
    // -----------------------
    @Test
    void toString_contem_infos_chave() {
        SubjectInmostVisit inmost = makeInmost();
        Subject subject = makeSubject(inmost);
        RequerimentoInfo r = makeReq(subject);

        String s = r.toString();
        assertTrue(s.contains("RequerimentoInfo"));
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("name_visited=test"));
        assertTrue(s.contains("cpf_rne=123456789002"));
    }

    // -----------------------
    // Cobertura equals/hashCode (reflexivo, simétrico, transitivo)
    // -----------------------
    @Test
    void equals_hashCode_reflexivo_simetrico_transitivo() {
        // Use as MESMAS instâncias de subject/inmost para que todos os campos sejam idênticos
        SubjectInmostVisit inmost = makeInmost();
        Subject subject = makeSubject(inmost);

        RequerimentoInfo r1 = makeReq(subject);
        RequerimentoInfo r2 = makeReq(subject);
        RequerimentoInfo r3 = makeReq(subject);

        // Reflexivo
        assertEquals(r1, r1);

        // Simétrico
        assertEquals(r1, r2);
        assertEquals(r2, r1);

        // Transitivo
        assertEquals(r2, r3);
        assertEquals(r1, r3);

        // hashCode consistente quando equals é true
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    // -----------------------
    // Ramo "diferente" de equals
    // -----------------------
    @Test
    void equals_diferente_quando_um_campo_muda() {
        SubjectInmostVisit inmost = makeInmost();
        Subject subject = makeSubject(inmost);

        RequerimentoInfo r1 = makeReq(subject);
        RequerimentoInfo r2 = makeReq(subject);
        assertEquals(r1, r2); // começam iguais

        // Mudar apenas 1 campo participante de equals/hashCode
        r2.setCity("outra-cidade");
        assertNotEquals(r1, r2);
    }

    // -----------------------
    // Nulos e outra classe
    // -----------------------
    @Test
    void equals_com_null_e_outra_classe_false() {
        SubjectInmostVisit inmost = makeInmost();
        RequerimentoInfo r = makeReq(makeSubject(inmost));

        assertNotEquals(r, null);
        assertNotEquals(r, "nao-sou-requerimento");
    }

    // -----------------------
    // Ramo de canEqual(false) (Lombok)
    // -----------------------
    @Test
    void equals_respeita_canEqual_quando_subclasse_nao_permita() {
        class RequerimentoInfoSub extends RequerimentoInfo {
            @Override
            public boolean canEqual(Object other) {
                // Força o caminho em que o Lombok retorna false em equals
                return false;
            }
        }

        SubjectInmostVisit inmost = makeInmost();
        Subject subject = makeSubject(inmost);

        RequerimentoInfo base = makeReq(subject);
        RequerimentoInfoSub sub = new RequerimentoInfoSub();

        // alinhar campos (mesmo assim equals deve retornar false por causa do canEqual=false)
        sub.setId(base.getId());
        sub.setName_visited(base.getName_visited());
        sub.setCpf_rne(base.getCpf_rne());
        sub.setType_visitation(base.getType_visitation());
        sub.setCellphone(base.getCellphone());
        sub.setState(base.getState());
        sub.setCity(base.getCity());
        sub.setDistrict(base.getDistrict());
        sub.setStreet(base.getStreet());
        sub.setNumber_house(base.getNumber_house());
        sub.setSubject(base.getSubject());

        assertFalse(base.equals(sub));
    }

    // -----------------------
    // Sanidade do relacionamento com Subject
    // -----------------------
    @Test
    void relacionamento_subject_ok() {
        SubjectInmostVisit inmost = makeInmost();
        Subject subject = makeSubject(inmost);
        RequerimentoInfo r = makeReq(subject);

        assertNotNull(r.getSubject());
        assertEquals("teste", r.getSubject().getSubject());
        assertNotNull(r.getSubject().getId_inmost_visit());
        assertTrue(r.getSubject().getId_inmost_visit().getPregnancy());
    }
}
