package com.github.sysdepen.depen_api.Services;

import com.github.sysdepen.depen_api.entity.Subject;
import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import com.github.sysdepen.depen_api.repository.SubjectRepository;
import com.github.sysdepen.depen_api.services.SubjectService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubjectServiceTest")
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject defaultSubject;


    @BeforeEach
    void setUp() {
        defaultSubject = sub(1L);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(subjectRepository);
    }

    @Test
    @DisplayName("save deve retornar o Subject está salvo")
    void save_ShouldReturnSavedEntity() {
        when(subjectRepository.save(defaultSubject)).thenReturn(defaultSubject);

        Subject saved = subjectService.save(defaultSubject);

        assertNotNull(saved);
        assertEquals("teste", defaultSubject.getSubject());
        verify(subjectRepository, times(1)).save(defaultSubject);
    }

    @Test
    @DisplayName("save deve propagar exceção do repository")
    void save_ShouldPropagateException() {
        when(subjectRepository.save(defaultSubject))
                .thenThrow(new RuntimeException("Generic save error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> subjectService.save(defaultSubject));

        assertEquals("Generic save error", ex.getMessage());
        verify(subjectRepository, times(1)).save(defaultSubject);
    }

    @Test
    @DisplayName("findAll deve retornar lista com registros")
    void findAll_ShouldReturnList() {
        when(subjectRepository.findAll()).thenReturn(List.of(defaultSubject));

        List<Subject> result = subjectService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia")
    void findAll_ShouldReturnEmptyList() {
        when(subjectRepository.findAll()).thenReturn(Collections.emptyList());

        List<Subject> result = subjectService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar Optional com entidade quando existir")
    void findById_ShouldReturnWhenExists() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(defaultSubject));

        Optional<Subject> found = subjectService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(defaultSubject, found.get());
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve retornar Optional.empty quando não existir")
    void findById_ShouldReturnEmptyWhenNotExists() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Subject> found = subjectService.findById(1L);

        assertTrue(found.isEmpty());
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("update deve salvar e retornar a entidade atualizada")
    void update_ShouldReturnUpdatedEntity() {
        when(subjectRepository.save(defaultSubject)).thenReturn(defaultSubject);

        Subject updated = subjectService.update(defaultSubject);

        assertNotNull(updated);
        verify(subjectRepository, times(1)).save(defaultSubject);
    }

    @Test
    @DisplayName("deleteById deve deletar e retornar true quando existir")
    void deleteById_ShouldDeleteAndReturnTrue_WhenExists() {
        when(subjectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(subjectRepository).deleteById(1L);

        boolean result = subjectService.deleteById(1L);

        assertTrue(result);
        verify(subjectRepository, times(1)).existsById(1L);
        verify(subjectRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById deve retornar false e não deletar quando não existir")
    void deleteById_ShouldReturnFalse_WhenNotExists() {
        when(subjectRepository.existsById(1L)).thenReturn(false);

        boolean result = subjectService.deleteById(1L);

        assertFalse(result);
        verify(subjectRepository, times(1)).existsById(1L);
        verify(subjectRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteById deve propagar exceção do repository")
    void deleteById_ShouldPropagateException() {
        when(subjectRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Generic delete error")).when(subjectRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> subjectService.deleteById(1L));

        assertEquals("Generic delete error", ex.getMessage());
        verify(subjectRepository, times(1)).existsById(1L);
        verify(subjectRepository, times(1)).deleteById(1L);
    }

    private static Subject sub(Long id) {
        SubjectInmostVisit subIn = new SubjectInmostVisit();
        subIn.setId(1L);
        subIn.setAccomplice("sim");
        subIn.setVictim(false);
        subIn.setPregnancy(true);
        subIn.setTime_pregnancy("4");
        subIn.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subIn.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        Subject subject = new Subject();
        subject.setId(id);
        subject.setSubject("teste");
        subject.setId_inmost_visit(subIn);
        subject.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        subject.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        return subject;
    }
}
