package com.github.sysdepen.depen_api.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysdepen.depen_api.security.auth.Login;
import com.github.sysdepen.depen_api.security.auth.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("LOGIN # 200 OK # retorna token no corpo")
    void logar_deveRetornar200_comToken() throws Exception {
        String token = "fake.jwt.token";
        when(loginService.logar(any(Login.class))).thenReturn(token);

        String json = """
          {"document":"12189350905","password":"Segredo123"}
        """;

        mockMvc.perform(post("/api/v1/login/logar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    @DisplayName("LOGIN # 401 Unauthorized # BadCredentialsException")
    void logar_deveRetornar401_quandoCredenciaisInvalidas() throws Exception {
        when(loginService.logar(any(Login.class)))
                .thenThrow(new BadCredentialsException("usuário/senha inválidos"));

        String json = """
          {"document":"11111111111","password":"errada"}
        """;

        mockMvc.perform(post("/api/v1/login/logar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Falha na autenticação")));
    }

    @Test
    @DisplayName("LOGIN # 400 Bad Request # exceção genérica do service")
    void logar_deveRetornar400_quandoServiceLancaExcecao() throws Exception {
        when(loginService.logar(any(Login.class)))
                .thenThrow(new RuntimeException("falha inesperada"));

        String json = """
          {"document":"11111111111","password":"Segredo123"}
        """;

        mockMvc.perform(post("/api/v1/login/logar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Erro ao realizar login")));
    }

    @Test
    @DisplayName("LOGIN # 400 Bad Request # payload inválido (violação de @Valid)")
    void logar_deveRetornar400_quandoPayloadInvalido() throws Exception {
        // Envia campos faltando/invalidos para acionar a validação do @Valid em Login
        String jsonInvalido = """
          {"email":"invalido"} 
        """;

        mockMvc.perform(post("/api/v1/login/logar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

}
