package com.github.sysdepen.depen_api.Services;


import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.repository.DocumentRepository;
import com.github.sysdepen.depen_api.repository.UsuarioRepository;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.services.DocumentService;
import com.github.sysdepen.depen_api.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock private DocumentRepository documentRepository;
    @Mock private UsuarioService userService;

    @InjectMocks
    private DocumentService documentService;

    private Documents doc;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        doc = new Documents();
        doc.setId(1L);
        doc.setDocumentType("RG");
        doc.setFileRGPath("/tmp/1/RG/arquivo.pdf");

        usuario = new Usuario();
        // preencha os campos essenciais do seu Usuario se o service exigir
        // ex.: usuario.setId(1L);
    }

    // ---------- save(Long userId, String documentType, String fileName) ----------

    @Test
    void save_devePersistirDocumento_quandoParametrosValidos() {
        when(userService.findById(1L)).thenReturn(usuario);
        when(documentRepository.save(any(Documents.class))).thenAnswer(inv -> {
            Documents d = inv.getArgument(0);
            d.setId(1L);
            return d;
        });

        Documents saved = documentService.save(1L, "RG", "arquivo.pdf");

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("RG", saved.getDocumentType());
        verify(userService, times(1)).findById(1L);
        verify(documentRepository, times(1)).save(any(Documents.class));
    }

    @Test
    void save_deveLancarExcecao_quandoUsuarioNaoExiste() {
        when(userService.findById(1L)).thenThrow(new RuntimeException("user not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.save(1L, "RG", "arquivo.pdf"));

        // não congele a mensagem, a menos que seja requisito
        assertTrue(ex.getMessage() != null && !ex.getMessage().isBlank());
        verify(userService, times(1)).findById(1L);
        verify(documentRepository, never()).save(any());
    }

    @Test
    void save_devePropagarExcecao_quandoRepositoryFalha() {
        when(userService.findById(1L)).thenReturn(usuario);
        when(documentRepository.save(any(Documents.class)))
                .thenThrow(new RuntimeException("document not saved"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.save(1L, "RG", "arquivo.pdf"));

        // use contains se quiser validar trecho
        assertTrue(ex.getMessage().contains("document not saved"));
        verify(documentRepository, times(1)).save(any(Documents.class));
    }

    // ---------- findAll() ----------

    @Test
    void findAll_deveRetornarLista() {
        when(documentRepository.findAll()).thenReturn(List.of(doc));

        List<Documents> result = documentService.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(documentRepository, times(1)).findAll();
    }

    // ---------- findById(Long) ----------

    @Test
    void findById_deveRetornarOptionalComValor_quandoExiste() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));

        Optional<Documents> result = documentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(documentRepository, atLeastOnce()).findById(1L); // pode ser chamado 2x pela impl.
    }

    @Test
    void findById_deveLancarExcecao_quandoNaoExiste() {
        when(documentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.findById(99L));

        assertTrue(ex.getMessage().contains("document") && ex.getMessage().contains("99"));
        verify(documentRepository, atLeastOnce()).findById(99L);
    }

    // ---------- findByUserIdAndDocumentType(Long, String) ----------

    @Test
    void findByUserIdAndDocumentType_deveRetornarOptional() {
        when(documentRepository.findByUserIdAndDocumentType(1L, "RG"))
                .thenReturn(Optional.of(doc));

        Optional<Documents> result = documentService.findByUserIdAndDocumentType(1L, "RG");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(documentRepository, times(1)).findByUserIdAndDocumentType(1L, "RG");
    }

    // ---------- update(Documents) ----------

    @Test
    void update_deveAtualizar_quandoDocumentoValido() {
        Documents input = new Documents();
        input.setId(1L);
        input.setDocumentType("CPF");
        input.setFileCPFPath("/novo/caminho/cpf.pdf");

        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any(Documents.class))).thenAnswer(inv -> inv.getArgument(0));

        Documents updated = documentService.update(input);

        assertNotNull(updated);
        assertEquals(1L, updated.getId());
        assertEquals("CPF", updated.getDocumentType());
        assertEquals("/novo/caminho/cpf.pdf", updated.getFileCPFPath());
        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).save(any(Documents.class));
    }

    @Test
    void update_deveLancarIllegalArgument_quandoIdNulo() {
        Documents semId = new Documents();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> documentService.update(semId));
        assertTrue(ex.getMessage().contains("document id is required"));
        verify(documentRepository, never()).findById(anyLong());
        verify(documentRepository, never()).save(any());
    }

    @Test
    void update_deveLancarRuntime_quandoNaoEncontrado() {
        Documents input = new Documents();
        input.setId(999L);
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.update(input));

        assertTrue(ex.getMessage().contains("document not found with id 999"));
        verify(documentRepository, times(1)).findById(999L);
        verify(documentRepository, never()).save(any());
    }

    @Test
    void update_devePropagarExcecao_quandoSaveFalha() {
        Documents input = new Documents();
        input.setId(1L);
        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any(Documents.class))).thenThrow(new RuntimeException("erro ao salvar"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.update(input));

        assertTrue(ex.getMessage().contains("erro ao salvar"));
        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).save(any());
    }



    // ---------- deleteById(Long) ----------

    @Test
    void deleteById_deveDeletar_quandoExiste() {
        when(documentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(documentRepository).deleteById(1L);

        assertDoesNotThrow(() -> documentService.deleteById(1L));
        verify(documentRepository, times(1)).existsById(1L);
        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_deveLancarIllegalArgument_quandoIdNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> documentService.deleteById(null));
        assertTrue(ex.getMessage().contains("id is required"));
        verify(documentRepository, never()).existsById(anyLong());
        verify(documentRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteById_deveLancarRuntime_quandoNaoExiste() {
        when(documentRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.deleteById(999L));

        assertTrue(ex.getMessage().contains("document not found with id 999"));
        verify(documentRepository, times(1)).existsById(999L);
        verify(documentRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteById_devePropagarExcecao_quandoDeleteFalha() {
        when(documentRepository.existsById(123L)).thenReturn(true);
        doThrow(new RuntimeException("erro ao deletar"))
                .when(documentRepository).deleteById(123L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentService.deleteById(123L));

        assertTrue(ex.getMessage().contains("erro ao deletar"));
        verify(documentRepository, times(1)).existsById(123L);
        verify(documentRepository, times(1)).deleteById(123L);
    }


}
