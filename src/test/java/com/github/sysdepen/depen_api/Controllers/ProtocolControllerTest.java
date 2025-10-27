package com.github.sysdepen.depen_api.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysdepen.depen_api.controller.ProtocolController;
import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.services.ProtocoloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProtocolControllerTest {

    @Autowired
    ProtocolController protocolController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private ProtocoloService protocoloService;

    Protocols protocols = new Protocols();

    Usuario usuario = new Usuario();

    @BeforeEach
    void setup() {
        String role = "admin";
        usuario.setId(1L);
        usuario.setName("Test");
        usuario.setDocument("123456789002");
        usuario.setEmail("kwanza@email.com");
        usuario.setPassword("123456");
        usuario.setRole("default");
        usuario.setDate_born(LocalDateTime.parse("2024-06-24T22:32:00"));
        usuario.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        usuario.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        protocols.setId(1L);
        protocols.setUser(usuario);
        protocols.setDoc(null);
        protocols.setAdmin(null);
        protocols.setStatus(1L);
        protocols.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        protocols.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
    }

    @Test
    void create_deveRetornar201() throws Exception {
        when(protocoloService.save(any())).thenReturn(protocols);

        String json = objectMapper.writeValueAsString(protocols);

        mockMvc.perform(post("/api/v1/protocols/save")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void createError_deveRetornar400() throws Exception {
        when(protocoloService.save(any())).thenReturn(protocols);
        protocols.setStatus(null);

        String json = objectMapper.writeValueAsString(protocols);

        mockMvc.perform(post("/api/v1/protocols/save")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_deveRetornar200() throws Exception {
        when (protocoloService.update(any())).thenReturn(protocols);

        String json = objectMapper.writeValueAsString(protocols);

        mockMvc.perform(put("/api/v1/protocols/update/" + protocols.getId())
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(json))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void delete_deveRetornar204_quandoSucesso() throws Exception {
        Long id = 1L;
        when(protocoloService.deleteById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/protocols/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_deveRetornar404_quandoNaoExiste() throws Exception {
        Long id = 999L;
        when(protocoloService.deleteById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/protocols/{id}", id))
                .andExpect(status().isNotFound());
    }

    Protocols p1 = new Protocols();
    Protocols p2 = new Protocols();


    @Test
    void findAll_deveRetornar200ELista() throws Exception {
        p1.setId(1L);
        p2.setId(2L);

        when(protocoloService.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/v1/protocols").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void findById_deveRetornar200() throws Exception {
        when(protocoloService.findById(1L)).thenReturn(Optional.of(p1));

        mockMvc.perform(get("/api/v1/protocols/" + 1L).accept("application/json"))
                .andExpect(status().isOk());
    }


    @Test
    void findById_quandoNaoExiste_deveRetornar404() throws Exception {
        when(protocoloService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/protocols/{id}", 999L))
                .andExpect(status().isNotFound());
    }


}
