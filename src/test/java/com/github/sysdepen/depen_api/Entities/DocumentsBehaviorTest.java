package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentsBehaviorTest {
    private Documents base() {
        Documents d = new Documents();
        d.setDocumentType("RG");
        d.setFileRGPath("/docs/rg.pdf");
        d.setFileCPFPath("/docs/cpf.pdf");
        d.setFileEnderecoPath("/docs/endereco.pdf");
        d.setFileFotoPath("/docs/foto.jpg");
        d.setFileAntCriminaisPath("/docs/ant_criminais.pdf");
        d.setFileGrauParentescoPath("/docs/grau.pdf");
        d.setUser(new Usuario());
        return d;
    }

    @Test
    void construtor_define_datas_automaticamente() {
        Documents d = new Documents();
        assertNotNull(d.getCreated_at());
        assertNotNull(d.getUpdated_at());
        assertTrue(d.getCreated_at().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void setters_funcionam_e_getters_retorna_valores() {
        Documents d = base();
        LocalDateTime fixed = LocalDateTime.of(2025, 1, 1, 12, 0);
        d.setId(10L);
        d.setCreated_at(fixed);
        d.setUpdated_at(fixed.plusDays(1));

        assertEquals(10L, d.getId());
        assertEquals(fixed, d.getCreated_at());
        assertEquals(fixed.plusDays(1), d.getUpdated_at());
        assertEquals("RG", d.getDocumentType());
        assertTrue(d.getFileRGPath().contains("rg.pdf"));
    }

    @Test
    void relacionamento_usuario_ok() {
        Documents d = base();
        Usuario u = new Usuario();
        u.setName("João");
        d.setUser(u);

        assertEquals("João", d.getUser().getName());
        assertNotNull(d.getUser());
    }

    @Test
    void arquivo_transient_pode_ser_definido_e_recuperado() {
        Documents d = new Documents();
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "arquivo.pdf", "application/pdf", "conteudo".getBytes()
        );
        d.setArquivo(file);
        assertEquals("arquivo.pdf", d.getArquivo().getOriginalFilename());
    }

    @Test
    void setName_nao_modifica_estado_e_nao_lanca_excecao() {
        Documents d = base();
        String antes = d.getDocumentType();
        assertDoesNotThrow(() -> d.setName("novoNome"));
        assertEquals(antes, d.getDocumentType());
    }

    @Test
    void datas_podem_ser_nulas_sem_excecao() {
        Documents d = base();
        d.setCreated_at(null);
        d.setUpdated_at(null);
        assertNull(d.getCreated_at());
        assertNull(d.getUpdated_at());
    }

    @Test
    void igualdade_baseada_em_campos_do_lombok() {
        Documents d1 = base();
        Documents d2 = base();

        // Mesmos timestamps (o construtor usa now())
        var created = java.time.LocalDateTime.of(2025, 1, 1, 0, 0);
        var updated = java.time.LocalDateTime.of(2025, 1, 2, 0, 0);

        // Mesmo usuário (mesma instância ou objetos realmente iguais)
        var u = new com.github.sysdepen.depen_api.security.auth.Usuario();
        u.setId(99L);

        d1.setId(1L);                 d2.setId(1L);
        d1.setDocumentType("DOC");    d2.setDocumentType("DOC");
        d1.setCreated_at(created);    d2.setCreated_at(created);
        d1.setUpdated_at(updated);    d2.setUpdated_at(updated);
        d1.setUser(u);                d2.setUser(u);

        // Campo transient não participa normalmente, mas garanta consistência
        d1.setArquivo(null);          d2.setArquivo(null);

        // Agora todos os campos são iguais
        assertEquals(d1, d2);

        // Modifique um campo para quebrar a igualdade
        d2.setFileFotoPath("outro.jpg");
        assertNotEquals(d1, d2);
    }

    @Test
    void toString_retorna_informacoes_chave() {
        Documents d = base();
        d.setId(5L);
        String ts = d.toString();
        assertTrue(ts.contains("5"));
        assertTrue(ts.contains("RG"));
    }
}
