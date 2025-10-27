package com.github.sysdepen.depen_api.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.services.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DocumentsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private DocumentService documentService;

    @Test
    void findAll_deveRetornar200EListaDeDocumentos() throws Exception {
        Documents doc1 = new Documents(); doc1.setId(1L);
        Documents doc2 = new Documents(); doc2.setId(2L);
        when(documentService.findAll()).thenReturn(List.of(doc1, doc2));

        mockMvc.perform(get("/api/v1/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void findById_deveRetornar200_quandoExiste() throws Exception {
        Documents doc = new Documents(); doc.setId(1L);
        when(documentService.findById(1L)).thenReturn(Optional.of(doc));

        mockMvc.perform(get("/api/v1/documents/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_deveRetornar404_quandoNaoExiste() throws Exception {
        when(documentService.findById(99L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/v1/documents/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadFiles_deveRetornar200_quandoSucesso() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "files", "teste.pdf", "application/pdf", "conteudo".getBytes()
        );

        Documents savedDoc = new Documents(); savedDoc.setId(1L);
        when(documentService.save(any(), any(), any())).thenReturn(savedDoc);


        mockMvc.perform(multipart("/api/v1/documents/upload")
                        .file(mockFile)
                        .param("userId", "1")
                        .param("documentType", "CPF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void update_deveRetornar200_quandoSucesso() throws Exception {
        Documents doc = new Documents(); doc.setId(1L);
        when(documentService.update(any())).thenReturn(doc);

        String json = objectMapper.writeValueAsString(doc);

        mockMvc.perform(put("/api/v1/documents")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_deveRetornar200_quandoSucesso() throws Exception {
        mockMvc.perform(delete("/api/v1/documents/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getDocumentByUserAndType_deveRetornarArquivo_quandoExiste() throws Exception {
        // Cria arquivo temporário
        File tempFile = File.createTempFile("teste", ".pdf");
        Documents doc = new Documents();
        doc.setFileRGPath(tempFile.getAbsolutePath());

        when(documentService.findByUserIdAndDocumentType(1L, "RG"))
                .thenReturn(Optional.of(doc));

        mockMvc.perform(get("/api/v1/documents/documents")
                        .param("userId", "1")
                        .param("documentType", "RG"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("attachment")))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }


}
