package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Address;
import com.github.sysdepen.depen_api.entity.RequerimentoInfo;
import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AddressValidationTest {
    static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        validator = f.getValidator();
    }

    private Address novoValido() {
        Address address = new Address();

        address.setId(1L);
        address.setUF("PR");
        address.setCep("85884-000");
        address.setStreet("rua tal");
        address.setCountry("Brasil");
        address.setNumber_house("1260");
        address.setCity("Medianeira");
        address.setDistrict("bairro");
        return address;
    }

    @Test
    void equals_reflexivo() {

        Address a = novoValido();
        a.setId(99L);

        Address a1 = novoValido();
        a1.setUF("PR");

        Address a2 = novoValido();
        a2.setUF("PR");

        Address a3 = novoValido();
        a3.setUF("PR");

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
    void equals_retorna_false_quando_um_campo_diferente() {
        Address a1 = novoValido();
        Address a2 = novoValido();
        a2.setCity("OutraCidade"); // difere em 1 campo
        assertNotEquals(a1, a2);
    }

    @Test
    void equals_retorna_false_quando_um_campo_eh_null_e_outro_nao() {
        Address a1 = novoValido();
        Address a2 = novoValido();
        a1.setDistrict(null);      // null vs não-null
        assertNotEquals(a1, a2);
        assertNotEquals(a2, a1);   // simetria do false
    }

    @Test
    void equals_permanece_true_quando_campo_nulo_em_ambos() {
        Address a1 = novoValido();
        Address a2 = novoValido();
        a1.setDistrict(null);
        a2.setDistrict(null);      // null vs null: deve continuar igual
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void equals_e_hashCode_com_id_nulo() {
        Address a1 = novoValido();
        Address a2 = novoValido();
        a1.setId(null);
        a2.setId(null);            // cobre ramo de comparação de Long nulo
        assertEquals(a1, a2);
        // chamar hashCode para cobrir caminho com múltiplos campos nulos
        a1.hashCode();
        a2.hashCode();
    }

    @Test
    void equals_com_null_e_com_outra_classe_retorna_false() {
        Address d1 = novoValido();

        assertNotEquals(d1, null);           // null
        assertNotEquals(d1, "nao-sou-requerimento");  // outra classe
    }

    @Test
    void testGettersAndSetters() {
        Address address = novoValido();

        assertEquals(1L, address.getId());
        assertEquals("PR", address.getUF());
        assertEquals("Medianeira", address.getCity());
        assertEquals("bairro", address.getDistrict());
        assertEquals("rua tal", address.getStreet());
        assertEquals("1260", address.getNumber_house());

    }
}
