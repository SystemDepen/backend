package com.github.sysdepen.depen_api.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysdepen.depen_api.controller.UsuarioController;
import com.github.sysdepen.depen_api.repository.UsuarioRepository;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    UsuarioController userController;

    @MockBean
    UsuarioRepository userRepository;

    Usuario user = new Usuario();

    @MockBean private UsuarioService usuarioService;

    @Autowired private ObjectMapper objectMapper;


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        String role = "admin";
        user.setId(1L);
        user.setName("Test");
        user.setDocument("123456789002");
        user.setEmail("kwanza@email.com");
        user.setPassword("123456");
        user.setRole("default");
        user.setDate_born(LocalDateTime.parse("2024-06-24T22:32:00"));
        user.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        user.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
    }

    @Test
    void saveUser() {
        ResponseEntity<String> retorno = userController.save(user);

        assertEquals(HttpStatus.OK, retorno.getStatusCode());
    }

    @Test
    @DisplayName("USUÁRIO # RESPONSE: 'Email Inválido' # EXCEPTION")
    void saveUserError() {
        Usuario userFailed = new Usuario();
        userFailed.setEmail("asdasd");

        assertThrows(Exception.class, ()-> {
            ResponseEntity<String> message = userController.save(userFailed);
        });
    }

    @Test
    void save_deveRetornar400_quandoPayloadInvalido() throws Exception {
        String jsonInvalido = """
      {"email":"invalido"}  // faltam campos obrigatórios
    """;

        mockMvc.perform(post("/api/v1/usuario/save")
                        .contentType("application/json")
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).save(any());
    }


    @Test
    void save_deveRetornar400_quandoServiceLancaExcecao() throws Exception {
        when(usuarioService.save(any())).thenThrow(new RuntimeException("falha"));

        String json = """
      {"name":"Fulano","email":"fulano@ex.com","document":"123","password":"Segredo123"}
    """;

        mockMvc.perform(post("/api/v1/usuario/save")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_deveRetornar200() throws Exception {
        when(usuarioService.update(any(), anyLong())).thenReturn("Atualizado!");

        String json = """
      {"name":"Novo Nome","email":"novo@ex.com","document":"123","password":"NovaSenha"}
    """;

        mockMvc.perform(put("/api/v1/usuario/update/{id}", 1L)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Atualizado!"));
    }

    @Test
    void update_deveRetornar400_quandoPayloadInvalido() throws Exception {
        String jsonInvalido = """
                {"email":"x"}
                """;

        mockMvc.perform(put("/api/v1/usuario/update/{id}", 1L)
                        .contentType("application/json")
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).update(any(), anyLong());
    }

    @Test
    void update_deveRetornar400_quandoServiceLancaExcecao() throws Exception {
        when(usuarioService.update(any(), anyLong())).thenThrow(new RuntimeException("falha"));

        String json = """
      {"name":"Novo Nome","email":"novo@ex.com","document":"123","password":"NovaSenha"}
    """;

        mockMvc.perform(put("/api/v1/usuario/update/{id}", 1L)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_deveRetornar200() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(user); // 'user' do seu @BeforeEach

        mockMvc.perform(get("/api/v1/usuario/findById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_deveRetornar400_quandoServiceLancaExcecao() throws Exception {
        when(usuarioService.findById(999L)).thenThrow(new RuntimeException("não achou"));

        mockMvc.perform(get("/api/v1/usuario/findById/{id}", 999L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_deveRetornar200ELista() throws Exception {
        Usuario u2 = new Usuario(); u2.setId(2L);

        when(usuarioService.findAll()).thenReturn(java.util.List.of(user, u2));

        mockMvc.perform(get("/api/v1/usuario/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void findAll_deveRetornar400_quandoServiceLancaExcecao() throws Exception {
        when(usuarioService.findAll()).thenThrow(new RuntimeException("falha"));

        mockMvc.perform(get("/api/v1/usuario/findAll"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_deveRetornar204_quandoSucesso() throws Exception {
        when(usuarioService.delete(user.getId())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuario/{id}", user.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_deveRetornar404_quandoNaoExiste() throws Exception {
        Long id = 999L;
        when(usuarioService.delete(id)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/usuario/{id}", id))
                .andExpect(status().isNotFound());
    }
}
