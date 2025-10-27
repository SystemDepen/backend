package com.github.sysdepen.depen_api.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import com.github.sysdepen.depen_api.services.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SubjectControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private SubjectService subjectService;

    Subject subject = new Subject();

    SubjectInmostVisit subIn = new SubjectInmostVisit();

    @BeforeEach
    void setup(){
        subIn.setId(1L);
        subIn.setAccomplice("sim");
        subIn.setVictim(false);
        subIn.setPregnancy(true);
        subIn.setTime_pregnancy("4");
        subIn.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subIn.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        subject.setId(1L);
        subject.setSubject("teste");
        subject.setId_inmost_visit(subIn);
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
    }


    @Test
    void create_deveRetornar201() throws Exception {
        when(subjectService.save(any())).thenReturn(subject);

        String json = objectMapper.writeValueAsString(subject);

        mockMvc.perform(post("/api/v1/users/subject")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("create: payload inválido deve retornar 400")
    void create_payloadInvalido_deveRetornar400() throws Exception {
        subject.setSubject(null);
        String jsonInvalido = objectMapper.writeValueAsString(subject);

        mockMvc.perform(post("/api/v1/users/subject")
                        .contentType("application/json")
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_deveRetornar200ELista() throws Exception {
        Subject s2 = new Subject(); s2.setId(2L);
        when(subjectService.findAll()).thenReturn(List.of(subject, s2));

        mockMvc.perform(get("/api/v1/users/subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }


    @Test
    void findById_deveRetornar200() throws Exception {
        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/api/v1/users/subject/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    void findById_deveRetornar404_quandoNaoExiste() throws Exception {
        when(subjectService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/subject/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_deveRetornar200() throws Exception {
        when(subjectService.update(any())).thenReturn(subject);

        String json = objectMapper.writeValueAsString(subject);

        mockMvc.perform(put("/api/v1/users/subject/" + + subject.getId())
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        when(subjectService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/users/subject/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_deveRetornar404_quandoNaoExiste() throws Exception {
        when(subjectService.deleteById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/users/subject/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}