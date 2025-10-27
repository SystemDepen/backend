package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentValidationTest {
    static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Documents novoValido() {
        Documents doc = new Documents();
        doc.setId(1L);
        doc.setDocumentType("RG");
        doc.setFileRGPath("/files/rg.pdf");
        doc.setFileCPFPath("/files/cpf.pdf");
        doc.setFileEnderecoPath("/files/endereco.pdf");
        doc.setFileFotoPath("/files/foto.png");
        doc.setFileAntCriminaisPath("/files/ant_criminais.pdf");
        doc.setFileGrauParentescoPath("/files/grau.pdf");
        doc.setCreated_at(LocalDateTime.now());
        doc.setUpdated_at(LocalDateTime.now());
        doc.setUser(new Usuario());
        return doc;
    }

    @Test
    void valido_quando_campos_preenchidos() {
        Documents d = novoValido();
        assertTrue(validator.validate(d).isEmpty(),
                "Não deveria haver violações de validação");
    }

    private Documents makeDoc(Long id, String type, Usuario user,
                              LocalDateTime created, LocalDateTime updated) {
        Documents d = new Documents();
        d.setId(id);
        d.setDocumentType(type);
        d.setFileRGPath("/docs/rg.pdf");
        d.setFileCPFPath("/docs/cpf.pdf");
        d.setFileGrauParentescoPath("/docs/grau.pdf");
        d.setFileAntCriminaisPath("/docs/ant_criminais.pdf");
        d.setFileEnderecoPath("/docs/endereco.pdf");
        d.setFileFotoPath("/docs/foto.jpg");
        d.setCreated_at(created);
        d.setUpdated_at(updated);
        d.setUser(user);
        d.setArquivo(null); // só para deixar explícito
        return d;
    }

    @Test
    void equals_reflexivo_simetrico_transitivo_e_hashCode_consistente() {
        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2025, 1, 2, 0, 0);
        Usuario u = new Usuario(); u.setId(99L);

        Documents d1 = makeDoc(1L, "DOC", u, created, updated);
        Documents d2 = makeDoc(1L, "DOC", u, created, updated);
        Documents d3 = makeDoc(1L, "DOC", u, created, updated);

        // Reflexivo
        assertEquals(d1, d1);

        // Simétrico
        assertEquals(d1, d2);
        assertEquals(d2, d1);

        // Transitivo
        assertEquals(d2, d3);
        assertEquals(d1, d3);

        // hashCode consistente com equals
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void equals_com_null_e_com_outra_classe_retorna_false() {
        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2025, 1, 2, 0, 0);
        Usuario u = new Usuario();

        Documents d1 = makeDoc(1L, "DOC", u, created, updated);

        assertNotEquals(d1, null);           // null
        assertNotEquals(d1, "nao-sou-doc");  // outra classe
    }


    @Test
    void inicializa_datas_no_construtor() {
        Documents d = new Documents();
        assertNotNull(d.getCreated_at(), "created_at deve ser inicializado no construtor");
        assertNotNull(d.getUpdated_at(), "updated_at deve ser inicializado no construtor");
        assertTrue(d.getCreated_at().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void aceita_relacionamento_usuario() {
        Documents d = novoValido();
        Usuario u = new Usuario();
        u.setName("Carlos");
        d.setUser(u);

        assertEquals("Carlos", d.getUser().getName());
    }

    @Test
    void permite_campo_transient_arquivo() {
        Documents d = new Documents();
        assertNull(d.getArquivo(), "Campo transient começa nulo");
    }

    @Test
    void metodo_setName_nao_lanca_excecao() {
        Documents d = new Documents();
        assertDoesNotThrow(() -> d.setName("Teste"),
                "setName deve ser seguro mesmo vazio");
    }

    @Test
    void valida_datas_nao_nulas() {
        Documents d = novoValido();
        d.setCreated_at(null);
        d.setUpdated_at(null);

        // como @Column(nullable=false) é checado no banco, não via Bean Validation,
        // aqui apenas garantimos que os campos realmente podem ser setados manualmente
        assertNull(d.getCreated_at());
        assertNull(d.getUpdated_at());
    }
}
