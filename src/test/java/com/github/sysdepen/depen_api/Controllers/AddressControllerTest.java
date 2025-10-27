package com.github.sysdepen.depen_api.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysdepen.depen_api.controller.AddressController;
import com.github.sysdepen.depen_api.controller.UsuarioController;
import com.github.sysdepen.depen_api.entity.Address;
import com.github.sysdepen.depen_api.repository.AddressRepository;
import com.github.sysdepen.depen_api.repository.UsuarioRepository;
import com.github.sysdepen.depen_api.services.AddressService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private AddressService addressService;

    Address address = new Address();

    @BeforeEach
    void setup(){
        address.setId(1L);
        address.setUF("PR");
        address.setCep("85884-000");
        address.setStreet("rua tal");
        address.setCountry("Brasil");
        address.setNumber_house("1260");
        address.setCity("Medianeira");
        address.setDistrict("bairro");
    }

    @Test
    void create_deveRetornar201() throws Exception {
        when(addressService.save(any())).thenReturn(address);

        String json = objectMapper.writeValueAsString(address);

        mockMvc.perform(post("/api/v1/address/save")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_deveRetornar400_quandoPayloadInvalido() throws Exception {
        String jsonInvalido = """
          { "cep":"xx", "UF":"ASD" }
        """;

        mockMvc.perform(post("/api/v1/address/save")
                        .contentType("application/json")
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_deveRetornar200() throws Exception {
        when(addressService.update(any())).thenReturn(address);

        String json = objectMapper.writeValueAsString(address);

        mockMvc.perform(put("/api/v1/address/update/{id}", address.getId())
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void findAll_deveRetornar200ELista() throws Exception {
        Address a2 = new Address(); a2.setId(2L);
        when(addressService.findAll()).thenReturn(List.of(address, a2));

        mockMvc.perform(get("/api/v1/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }


    @Test
    void findById_deveRetornar200_quandoExiste() throws Exception {
        when(addressService.findById(1L)).thenReturn(Optional.of(address));

        mockMvc.perform(get("/api/v1/address/{id}", address.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_deveRetornar404_quandoNaoExiste() throws Exception {
        when(addressService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/address/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_deveRetornar204_quandoSucesso() throws Exception {
        when(addressService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/address/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_deveRetornar404_quandoNaoExiste() throws Exception {
        when(addressService.deleteById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/address/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}