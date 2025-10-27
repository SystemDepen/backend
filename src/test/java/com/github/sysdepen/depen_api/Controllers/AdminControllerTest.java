package com.github.sysdepen.depen_api.Controllers;


import com.github.sysdepen.depen_api.controller.AdminController;
import com.github.sysdepen.depen_api.entity.Admin;

import static org.mockito.Mockito.*;

import com.github.sysdepen.depen_api.services.AdminService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class AdminControllerTest {

    @Autowired
    AdminController adminController;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AdminService adminService;

    Admin admin = new Admin();

    @BeforeEach
    void setup() {
        Short role = 2;
        admin.setId(1L);
        admin.setName("Teste");
        admin.setDocument("121.893.509-05");
        admin.setEmail("farao@gmail.com");
        admin.setPassword("123456");
        admin.setRole(role);
        admin.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        admin.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
    }

    @Test
    void create_deveRetornar201() throws Exception {
        when(adminService.save(any())).thenReturn(admin);

        mockMvc.perform(post("/api/v1/admins")
                        .contentType("application/json")
                        .content("""
                { "name":"Fulano", "email":"fulano@exemplo.com", "password":"Segredo123" }
            """))
                .andExpect(status().isCreated());

        verify(adminService, times(1)).save(any(Admin.class));
    }

    @Test
    void create_deveRetornar400_quandoPayloadInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/admins")
                        .contentType("application/json")
                        .content("""
                                { "email":"invalido" }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_deveRetornar200() throws Exception {
        when(adminService.update(any())).thenReturn(admin);

        mockMvc.perform(put("/api/v1/admins")
                        .contentType("application/json")
                        .content("""
                { "id":1, "name":"Novo", "email":"novo@exemplo.com", "password":"NovaSenha" }
            """))
                .andExpect(status().isOk());

        verify(adminService, times(1)).update(any(Admin.class));
    }

    @Test
    void update_deveRetornar400_quandoPayloadInvalido() throws Exception {
        mockMvc.perform(put("/api/v1/admins")
                        .contentType("application/json")
                        .content("""
                                { "id":1, "email":"x" }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_deveRetornar200ELista() throws Exception {
        Admin a2 = new Admin();
        a2.setId(2L);

        when(adminService.findAll()).thenReturn(java.util.List.of(admin, a2));

        mockMvc.perform(get("/api/v1/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void findById_deveRetornar200_quandoExiste() throws Exception {
        when(adminService.findById(1L)).thenReturn(java.util.Optional.of(admin));

        mockMvc.perform(get("/api/v1/admins/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_deveRetornar404_quandoNaoExiste() throws Exception {
        when(adminService.findById(999L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/v1/admins/{id}", 999L))
                .andExpect(status().isNotFound());
    }


    @Test
    void saveAdmin() {
        ResponseEntity<Admin> retorno = adminController.create(admin);

        assertEquals(HttpStatus.CREATED, retorno.getStatusCode());
    }

    @Test
    @DisplayName("ADMIN ESTÁ COM EMAIL INVÁLIDO")
    void saveAdminError() {
        Admin adminFailed = new Admin();
        adminFailed.setEmail("xxzcxdasd");

        assertThrows(Exception.class, () -> {
            ResponseEntity<Admin> message = adminController.create(adminFailed);
        });
    }

    @Test
    @DisplayName("ADMIN ESTÁ COM DOCUMENTO INVÁLIDO")
    void saveAdminErrorDocument() {
        Admin adminFailed = new Admin();
        adminFailed.setDocument("xxzcxdasd");

        assertThrows(Exception.class, () -> {
            ResponseEntity<Admin> message = adminController.create(adminFailed);
        });
    }

    @Test
    void delete_deveRetornar204_quandoExiste() throws Exception {
        when(adminService.deleteById(1L)).thenReturn(true); // ou deleteById(1L)

        mockMvc.perform(delete("/api/v1/admins/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(adminService, times(1)).deleteById(1L); // ou deleteById(1L)
    }

    @Test
    void delete_deveRetornar404_quandoNaoExiste() throws Exception {
        when(adminService.deleteById(999L)).thenReturn(false); // ou deleteById(999L)

        mockMvc.perform(delete("/api/v1/admins/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).deleteById(999L); // ou deleteById(999L)
    }

}