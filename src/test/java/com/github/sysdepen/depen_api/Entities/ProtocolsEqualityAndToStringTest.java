package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.*;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class ProtocolsEqualityAndToStringTest {
    private Usuario mkUser(long id) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setName("User" + id);
        u.setDocument("1234567890" + id);
        u.setEmail("user"+id+"@email.com");
        u.setPassword("123456");
        u.setRole("ROLE_USER");
        u.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        u.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        return u;
    }

    private Admin mkAdmin(long id) {
        Admin a = new Admin();
        a.setId(id);
        a.setName("Admin" + id);
        a.setDocument("12345678900"); // formato válido
        a.setEmail("admin"+id+"@email.com");
        a.setPassword("Senha@123");
        a.setRole((short)1);
        a.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        a.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        return a;
    }

    private Documents mkDocs(long id) {
        Documents d = new Documents();
        d.setId(id);
        d.setDocumentType("DOC");
        d.setFileRGPath("/rg.pdf");
        d.setFileCPFPath("/cpf.pdf");
        d.setFileEnderecoPath("/end.pdf");
        d.setFileAntCriminaisPath("/ant.pdf");
        d.setFileGrauParentescoPath("/gp.pdf");
        d.setFileFotoPath("/foto.jpg");
        // sobrescreve para determinismo
        d.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        d.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        d.setUser(mkUser(9));
        return d;
    }

    private SubjectInmostVisit mkInmost() {
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

    private Subject mkSubject() {
        Subject subj = new Subject();
        subj.setId(1L);
        subj.setSubject("teste");
        subj.setId_inmost_visit(mkInmost());
        subj.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subj.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        return subj;
    }

    private RequerimentoInfo mkReqInfo() {
        RequerimentoInfo r = new RequerimentoInfo();
        r.setId(1L);
        r.setName_visited("visitado");
        r.setCpf_rne("12345678900");
        r.setType_visitation("tipo");
        r.setCellphone("45999999999");
        r.setState("PR");
        r.setCity("cidade");
        r.setDistrict("bairro");
        r.setStreet("rua");
        r.setNumber_house("10");
        r.setSubject(mkSubject());
        return r;
    }

    private Protocols mkProtoBase() {
        Protocols p = new Protocols();
        p.setId(1L);
        p.setStatus(1L);
        p.setUser(mkUser(1));
        p.setAdmin(mkAdmin(1));
        p.setDoc(mkDocs(1));
        p.setReq_info(mkReqInfo());
        p.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        p.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        return p;
    }

    // --------- Cobrir toString ---------
    @Test
    void toString_contem_infos_chave() {
        Protocols p = mkProtoBase();
        String s = p.toString();
        assertTrue(s.contains("Protocols"));
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("status=1"));
    }

    // --------- equals/hashCode: objetos idênticos ---------
    @Test
    void equals_hashCode_quando_todos_campos_iguais() {
        Protocols a = mkProtoBase();
        Usuario u = new Usuario();
        Documents doc = new Documents();
        a.setDoc(doc);
        a.setUser(u);
        Protocols b = mkProtoBase();
        b.setUser(u);
        b.setDoc(doc);
        assertEquals(a, a);            // reflexivo
        assertEquals(a, b);            // iguais
        assertEquals(b, a);            // simétrico
        assertEquals(a.hashCode(), b.hashCode()); // consistente
    }

    // --------- equals: ramos "diferente" para cada campo ---------
    @Test
    void equals_diferente_quando_muda_cada_campo() {
        Protocols base = mkProtoBase();

        // id diferente
        Protocols p = mkProtoBase(); p.setId(2L);
        assertNotEquals(base, p);

        // status diferente
        p = mkProtoBase(); p.setStatus(2L);
        assertNotEquals(base, p);

        // user diferente
        p = mkProtoBase(); p.setUser(mkUser(2));
        assertNotEquals(base, p);

        // admin diferente
        p = mkProtoBase(); p.setAdmin(mkAdmin(2));
        assertNotEquals(base, p);

        // doc diferente
        p = mkProtoBase(); p.setDoc(mkDocs(2));
        assertNotEquals(base, p);

        // req_info diferente
        RequerimentoInfo ri = mkReqInfo(); ri.setCity("outra");
        p = mkProtoBase(); p.setReq_info(ri);
        assertNotEquals(base, p);

        // created_at diferente
        p = mkProtoBase(); p.setCreated_at(LocalDateTime.parse("2024-06-25T00:00:00"));
        assertNotEquals(base, p);

        // updated_at diferente
        p = mkProtoBase(); p.setUpdated_at(LocalDateTime.parse("2024-06-25T00:00:00"));
        assertNotEquals(base, p);
    }

    // --------- equals: null e outra classe ---------
    @Test
    void equals_com_null_e_outra_classe_false() {
        Protocols base = mkProtoBase();
        assertNotEquals(base, null);
        assertNotEquals(base, "nao-sou-protocol");
    }

    // --------- canEqual(false) para cobrir o ramo interno do Lombok ---------
    @Test
    void equals_respeita_canEqual_quando_subclasse_nao_permita() {
        class ProtocolsSub extends Protocols {
            @Override public boolean canEqual(Object other) { return false; }
        }
        Protocols base = mkProtoBase();
        ProtocolsSub sub = new ProtocolsSub();

        // alinhar campos (mesmo assim equals deve retornar false por causa do canEqual=false)
        sub.setId(base.getId());
        sub.setStatus(base.getStatus());
        sub.setUser(base.getUser());
        sub.setAdmin(base.getAdmin());
        sub.setDoc(base.getDoc());
        sub.setReq_info(base.getReq_info());
        sub.setCreated_at(base.getCreated_at());
        sub.setUpdated_at(base.getUpdated_at());

        assertFalse(base.equals(sub));
    }

    // --------- hashCode: caminho com nulos ---------
    @Test
    void hashCode_funciona_com_campos_nulos() {
        // instancia "vazia" aciona ramos de null dentro do hash
        Protocols vazio = new Protocols();
        // não esperamos valor específico; só garantir que não lança
        assertDoesNotThrow(vazio::hashCode);
    }
}
