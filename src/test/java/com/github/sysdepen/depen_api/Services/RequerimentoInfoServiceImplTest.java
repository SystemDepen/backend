package com.github.sysdepen.depen_api.Services;


import com.github.sysdepen.depen_api.entity.RequerimentoInfo;
import com.github.sysdepen.depen_api.repository.RequerimentosInfoRepository;
import com.github.sysdepen.depen_api.services.RequerimentosInfoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequerimentosInfoService")
public class RequerimentoInfoServiceImplTest {
    @Mock
    private RequerimentosInfoRepository requerimentosInfoRepository;

    @InjectMocks
    private RequerimentosInfoService requerimentosInfoService;

    private RequerimentoInfo defaultReq;

    @BeforeEach
    void setUp() {
        defaultReq = req(1L, "Visitante X");
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(requerimentosInfoRepository);
    }

    @Test
    @DisplayName("save deve retornar o RequerimentoInfo salvo")
    void save_ShouldReturnSavedEntity() {
        when(requerimentosInfoRepository.save(defaultReq)).thenReturn(defaultReq);

        RequerimentoInfo saved = requerimentosInfoService.save(defaultReq);

        assertNotNull(saved);
        assertEquals("Visitante X", getNomeVisitante(saved));
        verify(requerimentosInfoRepository, times(1)).save(defaultReq);
    }


    @Test
    @DisplayName("save deve propagar exceção do repository")
    void save_ShouldPropagateException() {
        when(requerimentosInfoRepository.save(defaultReq))
                .thenThrow(new RuntimeException("Generic save error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> requerimentosInfoService.save(defaultReq));

        assertEquals("Generic save error", ex.getMessage());
        verify(requerimentosInfoRepository, times(1)).save(defaultReq);
    }

    @Test
    @DisplayName("findAll deve retornar lista com registros")
    void findAll_ShouldReturnList() {
        when(requerimentosInfoRepository.findAll()).thenReturn(List.of(defaultReq));

        List<RequerimentoInfo> result = requerimentosInfoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requerimentosInfoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia")
    void findAll_ShouldReturnEmptyList() {
        when(requerimentosInfoRepository.findAll()).thenReturn(Collections.emptyList());

        List<RequerimentoInfo> result = requerimentosInfoService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(requerimentosInfoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar Optional com entidade quando existir")
    void findById_ShouldReturnWhenExists() {
        when(requerimentosInfoRepository.findById(1L)).thenReturn(Optional.of(defaultReq));

        Optional<RequerimentoInfo> found = requerimentosInfoService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(defaultReq, found.get());
        verify(requerimentosInfoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve retornar Optional.empty quando não existir")
    void findById_ShouldReturnEmptyWhenNotExists() {
        when(requerimentosInfoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RequerimentoInfo> found = requerimentosInfoService.findById(1L);

        assertTrue(found.isEmpty());
        verify(requerimentosInfoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("update deve salvar e retornar a entidade atualizada")
    void update_ShouldReturnUpdatedEntity() {
        when(requerimentosInfoRepository.save(defaultReq)).thenReturn(defaultReq);

        RequerimentoInfo updated = requerimentosInfoService.update(defaultReq);

        assertNotNull(updated);
        verify(requerimentosInfoRepository, times(1)).save(defaultReq);
    }


    @Test
    @DisplayName("deleteById deve deletar e retornar true quando existir")
    void deleteById_ShouldDeleteAndReturnTrue_WhenExists() {
        when(requerimentosInfoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(requerimentosInfoRepository).deleteById(1L);

        boolean result = requerimentosInfoService.deleteById(1L);

        assertTrue(result);
        verify(requerimentosInfoRepository, times(1)).existsById(1L);
        verify(requerimentosInfoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById deve retornar false e não deletar quando não existir")
    void deleteById_ShouldReturnFalse_WhenNotExists() {
        when(requerimentosInfoRepository.existsById(1L)).thenReturn(false);

        boolean result = requerimentosInfoService.deleteById(1L);

        assertFalse(result);
        verify(requerimentosInfoRepository, times(1)).existsById(1L);
        verify(requerimentosInfoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteById deve propagar exceção do repository")
    void deleteById_ShouldPropagateException() {
        when(requerimentosInfoRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Generic delete error")).when(requerimentosInfoRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> requerimentosInfoService.deleteById(1L));

        assertEquals("Generic delete error", ex.getMessage());
        verify(requerimentosInfoRepository, times(1)).existsById(1L);
        verify(requerimentosInfoRepository, times(1)).deleteById(1L);
    }


    private static RequerimentoInfo req(Long id, String nomeVisitante) {
        RequerimentoInfo r = new RequerimentoInfo();
         r.setId(id); // se existir
         r.setName_visited(nomeVisitante); // ou o campo correto
        return r;
    }

    private static String getNomeVisitante(RequerimentoInfo r) {
         return r.getName_visited(); // ajuste para o getter real
    }
}
