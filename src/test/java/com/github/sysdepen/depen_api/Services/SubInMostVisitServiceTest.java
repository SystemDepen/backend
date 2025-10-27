package com.github.sysdepen.depen_api.Services;

import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import com.github.sysdepen.depen_api.repository.SubInMostVisitRepository;
import com.github.sysdepen.depen_api.services.SubInMostVisitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubInMostVisitServiceTest")
public class SubInMostVisitServiceTest {
    @Mock
    private SubInMostVisitRepository subInMostVisitRepository;

    @InjectMocks
    private SubInMostVisitService subInMostVisitService;

    private SubjectInmostVisit defaltSubject;

    @BeforeEach
    void setUp() {
        defaltSubject = sub(1L);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(subInMostVisitRepository);
    }

    @Test
    @DisplayName("save deve retornar o SubjectInmost está salvo")
    void save_ShouldReturnSavedEntity() {
        when(subInMostVisitRepository.save(defaltSubject)).thenReturn(defaltSubject);

        SubjectInmostVisit saved = subInMostVisitService.save(defaltSubject);

        assertNotNull(saved);
        assertEquals("sim", defaltSubject.getAccomplice());
        verify(subInMostVisitRepository, times(1)).save(defaltSubject);
    }

    @Test
    @DisplayName("save deve propagar exceção do repository")
    void save_ShouldPropagateException() {
        when(subInMostVisitRepository.save(defaltSubject))
                .thenThrow(new RuntimeException("Generic save error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> subInMostVisitService.save(defaltSubject));

        assertEquals("Generic save error", ex.getMessage());
        verify(subInMostVisitRepository, times(1)).save(defaltSubject);
    }

    @Test
    @DisplayName("findAll deve retornar lista com registros")
    void findAll_ShouldReturnList() {
        when(subInMostVisitRepository.findAll()).thenReturn(List.of(defaltSubject));

        List<SubjectInmostVisit> result = subInMostVisitService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subInMostVisitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia")
    void findAll_ShouldReturnEmptyList() {
        when(subInMostVisitRepository.findAll()).thenReturn(Collections.emptyList());

        List<SubjectInmostVisit> result = subInMostVisitService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subInMostVisitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar Optional com entidade quando existir")
    void findById_ShouldReturnWhenExists() {
        when(subInMostVisitRepository.findById(1L)).thenReturn(Optional.of(defaltSubject));

        Optional<SubjectInmostVisit> found = subInMostVisitService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(defaltSubject, found.get());
        verify(subInMostVisitRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve retornar Optional.empty quando não existir")
    void findById_ShouldReturnEmptyWhenNotExists() {
        when(subInMostVisitRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<SubjectInmostVisit> found = subInMostVisitService.findById(1L);

        assertTrue(found.isEmpty());
        verify(subInMostVisitRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("update deve salvar e retornar a entidade atualizada")
    void update_ShouldReturnUpdatedEntity() {
        when(subInMostVisitRepository.save(defaltSubject)).thenReturn(defaltSubject);

        SubjectInmostVisit updated = subInMostVisitService.update(defaltSubject);

        assertNotNull(updated);
        verify(subInMostVisitRepository, times(1)).save(defaltSubject);
    }

    @Test
    @DisplayName("deleteById deve deletar e retornar true quando existir")
    void deleteById_ShouldDeleteAndReturnTrue_WhenExists() {
        when(subInMostVisitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(subInMostVisitRepository).deleteById(1L);

        boolean result = subInMostVisitService.deleteById(1L);

        assertTrue(result);
        verify(subInMostVisitRepository, times(1)).existsById(1L);
        verify(subInMostVisitRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById deve retornar false e não deletar quando não existir")
    void deleteById_ShouldReturnFalse_WhenNotExists() {
        when(subInMostVisitRepository.existsById(1L)).thenReturn(false);

        boolean result = subInMostVisitService.deleteById(1L);

        assertFalse(result);
        verify(subInMostVisitRepository, times(1)).existsById(1L);
        verify(subInMostVisitRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteById deve propagar exceção do repository")
    void deleteById_ShouldPropagateException() {
        when(subInMostVisitRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Generic delete error")).when(subInMostVisitRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> subInMostVisitService.deleteById(1L));

        assertEquals("Generic delete error", ex.getMessage());
        verify(subInMostVisitRepository, times(1)).existsById(1L);
        verify(subInMostVisitRepository, times(1)).deleteById(1L);
    }

    private static SubjectInmostVisit sub(Long id) {
        SubjectInmostVisit subject = new SubjectInmostVisit();
        subject.setId(id);
        subject.setAccomplice("sim");
        subject.setVictim(false);
        subject.setPregnancy(true);
        subject.setTime_pregnancy("4");
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        return subject;
    }

}
