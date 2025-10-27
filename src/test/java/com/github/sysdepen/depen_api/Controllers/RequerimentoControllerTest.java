package com.github.sysdepen.depen_api.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.entity.RequerimentoInfo;
import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.services.ProtocoloService;
import com.github.sysdepen.depen_api.services.RequerimentosInfoService;
import org.junit.jupiter.api.BeforeEach;
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
public class RequerimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private RequerimentosInfoService requerimentosInfoService;

    RequerimentoInfo req = new RequerimentoInfo();

    Subject subject = new Subject();

    SubjectInmostVisit subIn = new SubjectInmostVisit();


    @BeforeEach
    void setup() {
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

        req.setId(1L);
        req.setName_visited("test");
        req.setCpf_rne("123456789002");
        req.setType_visitation("Teste");
        req.setCellphone("45988888888");
        req.setState("PR");
        req.setCity("medianeira");
        req.setDistrict("belo");
        req.setStreet("teste");
        req.setNumber_house("444a");
        req.setSubject(subject);
    }

    @Test
    void create_deveRetornar201() throws Exception {
        when(requerimentosInfoService.save(any())).thenReturn(req);

        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/v1/req_camp/save")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void createError_deveRetornar400() throws Exception {
        when(requerimentosInfoService.save(any())).thenReturn(req);
        req.setName_visited(null);

        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/v1/req_camp/save")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_deveRetornar200() throws Exception {
        when (requerimentosInfoService.update(any())).thenReturn(req);

        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(put("/api/v1/req_camp/" + req.getId())
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(json))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void findAll_deveRetornar200ELista() throws Exception {
        RequerimentoInfo r2 = new RequerimentoInfo(); r2.setId(2L);
        when(requerimentosInfoService.findAll()).thenReturn(List.of(req, r2));

        mockMvc.perform(get("/api/v1/req_camp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void findById_deveRetornar200() throws Exception {
        when(requerimentosInfoService.findById(1L)).thenReturn(Optional.of(req));

        mockMvc.perform(get("/api/v1/req_camp/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_quandoNaoExiste_deveRetornar404() throws Exception {
        when(requerimentosInfoService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/req_camp/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_deveRetornar204_quandoSucesso() throws Exception {
        Long id = 1L;
        when(requerimentosInfoService.deleteById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/req_camp/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_deveRetornar404_quandoNaoExiste() throws Exception {
        Long id = 999L;
        when(requerimentosInfoService.deleteById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/protocols/{id}", id))
                .andExpect(status().isNotFound());
    }

}
