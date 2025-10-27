package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentEnityUnit {
    @Test
    void constructorInicializaTimestamps() {
        // captura janelas de tempo para tolerância
        LocalDateTime antes = LocalDateTime.now();
        Documents doc = new Documents();
        LocalDateTime depois = LocalDateTime.now();

        assertNotNull(doc.getCreated_at());
        assertNotNull(doc.getUpdated_at());
        // criado/atualizado dentro da janela [antes, depois]
        assertTrue(!doc.getCreated_at().isBefore(antes) && !doc.getCreated_at().isAfter(depois));
        assertTrue(!doc.getUpdated_at().isBefore(antes) && !doc.getUpdated_at().isAfter(depois));
    }

    @Test
    void gettersESettersBasicosFuncionam() {
        Documents doc = new Documents();

        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setDocument("123");
        usuario.setRole("ROLE_USER");

        LocalDateTime fixed = LocalDateTime.of(2024, 1, 2, 3, 4, 5);

        doc.setId(1L);
        doc.setDocumentType("RG/CPF");
        doc.setFileRGPath("/path/rg.pdf");
        doc.setFileCPFPath("/path/cpf.pdf");
        doc.setFileGrauParentescoPath("/path/parentesco.pdf");
        doc.setFileAntCriminaisPath("/path/antecedentes.pdf");
        doc.setFileEnderecoPath("/path/endereco.pdf");
        doc.setFileFotoPath("/path/foto.jpg");
        doc.setCreated_at(fixed);
        doc.setUpdated_at(fixed.plusDays(1));
        doc.setUser(usuario);

        assertEquals(1L, doc.getId());
        assertEquals("RG/CPF", doc.getDocumentType());
        assertEquals("/path/rg.pdf", doc.getFileRGPath());
        assertEquals("/path/cpf.pdf", doc.getFileCPFPath());
        assertEquals("/path/parentesco.pdf", doc.getFileGrauParentescoPath());
        assertEquals("/path/antecedentes.pdf", doc.getFileAntCriminaisPath());
        assertEquals("/path/endereco.pdf", doc.getFileEnderecoPath());
        assertEquals("/path/foto.jpg", doc.getFileFotoPath());
        assertEquals(fixed, doc.getCreated_at());
        assertEquals(fixed.plusDays(1), doc.getUpdated_at());
        assertEquals(usuario, doc.getUser());
    }

    @Test
    void campoTransientArquivoPodeSerAtribuido() {
        Documents doc = new Documents();
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "doc.pdf", "application/pdf", "conteudo".getBytes()
        );
        doc.setArquivo(file);

        assertNotNull(doc.getArquivo());
        assertEquals("doc.pdf", doc.getArquivo().getOriginalFilename());
        assertEquals("application/pdf", doc.getArquivo().getContentType());
    }

    @Test
    void equalsEHashCodeDoLombokConsideramCampos() {
        Documents d1 = new Documents();
        Documents d2 = new Documents();

        // Para equals() do @Data funcionar de forma determinística, use valores fixos
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2024, 1, 2, 0, 0, 0);

        d1.setId(7L);
        d1.setDocumentType("TIPO");
        d1.setFileRGPath("a");
        d1.setFileCPFPath("b");
        d1.setFileGrauParentescoPath("c");
        d1.setFileAntCriminaisPath("d");
        d1.setFileEnderecoPath("e");
        d1.setFileFotoPath("f");
        d1.setCreated_at(created);
        d1.setUpdated_at(updated);

        d2.setId(7L);
        d2.setDocumentType("TIPO");
        d2.setFileRGPath("a");
        d2.setFileCPFPath("b");
        d2.setFileGrauParentescoPath("c");
        d2.setFileAntCriminaisPath("d");
        d2.setFileEnderecoPath("e");
        d2.setFileFotoPath("f");
        d2.setCreated_at(created);
        d2.setUpdated_at(updated);

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());

        // diferencie alterando um campo
        d2.setFileFotoPath("g");
        assertNotEquals(d1, d2);
    }

    @Test
    void toStringContemCamposImportantes() {
        Documents doc = new Documents();
        doc.setId(33L);
        doc.setDocumentType("DOC-TYPE");

        String s = doc.toString();
        assertTrue(s.contains("33"));
        assertTrue(s.contains("DOC-TYPE"));
    }

    @Test
    void setNameNaoAlteraEstado() {
        Documents doc = new Documents();
        doc.setDocumentType("ANTES");

        // snapshot de alguns campos
        Long oldId = doc.getId();
        String oldType = doc.getDocumentType();

        doc.setName("TestDocument");

        assertEquals(oldId, doc.getId());
        assertEquals(oldType, doc.getDocumentType());
    }
}
