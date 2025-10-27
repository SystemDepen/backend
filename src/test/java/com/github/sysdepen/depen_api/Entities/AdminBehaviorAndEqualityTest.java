package com.github.sysdepen.depen_api.Entities;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.entity.Protocols;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminBehaviorAndEqualityTest {

    private Admin novoAdminBase(LocalDateTime created, LocalDateTime updated) {
        Admin a = new Admin();
        a.setId(1L);
        a.setName("Ana Admin");
        a.setDocument("12345678900");  // válido pelo seu @Pattern
        a.setEmail("ana@exemplo.com");
        a.setPassword("Senha@123");
        a.setRole((short) 1);
        a.setCreated_at(created);
        a.setUpdated_at(updated);
        return a;
    }

    @Test
    void toString_contem_infos_chave() {
        var created = LocalDateTime.of(2025, 1, 1, 0, 0);
        var updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Admin a = novoAdminBase(created, updated);
        String s = a.toString();

        assertTrue(s.contains("Admin"));
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("Ana Admin"));
    }

    @Test
    void setEGetProtocols_funciona() {
        var created = LocalDateTime.of(2025, 1, 1, 0, 0);
        var updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Admin a = novoAdminBase(created, updated);

        Protocols p1 = new Protocols();
        Protocols p2 = new Protocols();
        List<Protocols> lista = new ArrayList<>();
        lista.add(p1);
        lista.add(p2);

        a.setProtocols(lista);
        assertNotNull(a.getProtocols());
        assertEquals(2, a.getProtocols().size());
        assertSame(lista, a.getProtocols());
    }

    @Test
    void equals_hashCode_reflexivo_simetrico_transitivo() {
        var created = LocalDateTime.of(2025, 1, 1, 0, 0);
        var updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        // Mesmos valores em TODOS os campos que participam de equals/hashCode
        Admin a1 = novoAdminBase(created, updated);
        Admin a2 = novoAdminBase(created, updated);
        Admin a3 = novoAdminBase(created, updated);

        // Para evitar diferenças de lista, use a MESMA instância
        List<Protocols> mesmaLista = new ArrayList<>();
        mesmaLista.add(new Protocols());
        a1.setProtocols(mesmaLista);
        a2.setProtocols(mesmaLista);
        a3.setProtocols(mesmaLista);

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
    void equals_diferente_quando_algum_campo_muda() {
        var created = LocalDateTime.of(2025, 1, 1, 0, 0);
        var updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Admin a1 = novoAdminBase(created, updated);
        Admin a2 = novoAdminBase(created, updated);

        // Mesmo conteúdo inicial
        List<Protocols> mesmaLista = new ArrayList<>();
        a1.setProtocols(mesmaLista);
        a2.setProtocols(mesmaLista);
        assertEquals(a1, a2);

        // Mude um campo que entra em equals/hashCode (ex.: name)
        a2.setName("Outra Admin");
        assertNotEquals(a1, a2);
    }

    @Test
    void equals_com_null_e_outra_classe_false() {
        var created = LocalDateTime.of(2025, 1, 1, 0, 0);
        var updated = LocalDateTime.of(2025, 1, 2, 0, 0);
        Admin a = novoAdminBase(created, updated);

        assertNotEquals(a, null);
        assertNotEquals(a, "nao sou Admin");
    }

    @Test
    void equals_respeita_canEqual_quando_subclasse_nao_permita() {
        class AdminSub extends Admin {
            @Override
            public boolean canEqual(Object other) {
                // força o ramo canEqual == false
                return false;
            }
        }

        var created = LocalDateTime.of(2025, 1, 1, 0, 0);
        var updated = LocalDateTime.of(2025, 1, 2, 0, 0);

        Admin base = novoAdminBase(created, updated);
        AdminSub sub = new AdminSub();
        sub.setId(base.getId());
        sub.setName(base.getName());
        sub.setDocument(base.getDocument());
        sub.setEmail(base.getEmail());
        sub.setPassword(base.getPassword());
        sub.setRole(base.getRole());
        sub.setCreated_at(created);
        sub.setUpdated_at(updated);
        sub.setProtocols(base.getProtocols());

        // Mesmo com campos iguais, canEqual=false deve fazer equals retornar false
        assertFalse(base.equals(sub));
    }
}
