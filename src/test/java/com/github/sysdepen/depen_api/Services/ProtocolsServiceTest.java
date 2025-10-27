package com.github.sysdepen.depen_api.Services;

import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.entity.SubjectInmostVisit;
import com.github.sysdepen.depen_api.repository.ProtocoloRepository;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.services.ProtocoloService;
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
@DisplayName("ProtocoloServiceTest")
public class ProtocolsServiceTest {
    @Mock
    private ProtocoloRepository protocoloRepository;

    @InjectMocks
    private ProtocoloService protocoloService;

    private Protocols defaultProtocols;

    @BeforeEach
    void setUp() {
        defaultProtocols = prot(1L);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(protocoloRepository);
    }

    @Test
    @DisplayName("save deve retornar o Protocolo quando salvo")
    void save_ShouldReturnSavedEntity() {
        when(protocoloRepository.save(any(Protocols.class))).thenReturn(defaultProtocols);

        Protocols saved = protocoloService.save(defaultProtocols);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(protocoloRepository, times(1)).save(defaultProtocols);
        verifyNoMoreInteractions(protocoloRepository);
    }

    @Test
    @DisplayName("save deve propagar exceção do repository")
    void save_ShouldPropagateException() {
        when(protocoloRepository.save(any(Protocols.class)))
                .thenThrow(new RuntimeException("Generic save error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> protocoloService.save(defaultProtocols));

        assertEquals("Generic save error", ex.getMessage());
        verify(protocoloRepository, times(1)).save(defaultProtocols);
        verifyNoMoreInteractions(protocoloRepository);
    }

    @Test
    @DisplayName("findAll deve retornar lista com registros")
    void findAll_ShouldReturnList() {
        when(protocoloRepository.findAll()).thenReturn(List.of(defaultProtocols));

        List<Protocols> result = protocoloService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(protocoloRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia")
    void findAll_ShouldReturnEmptyList() {
        when(protocoloRepository.findAll()).thenReturn(Collections.emptyList());

        List<Protocols> result = protocoloService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(protocoloRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar Optional com entidade quando existir")
    void findById_ShouldReturnWhenExists() {
        when(protocoloRepository.findById(1L)).thenReturn(Optional.of(defaultProtocols));

        Optional<Protocols> found = protocoloService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(defaultProtocols, found.get());
        verify(protocoloRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve retornar Optional.empty quando não existir")
    void findById_ShouldReturnEmptyWhenNotExists() {
        when(protocoloRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Protocols> found = protocoloService.findById(1L);

        assertTrue(found.isEmpty());
        verify(protocoloRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("update deve salvar e retornar a entidade atualizada")
    void update_ShouldReturnUpdatedEntity() {
        when(protocoloRepository.save(defaultProtocols)).thenReturn(defaultProtocols);

        Protocols updated = protocoloService.update(defaultProtocols);

        assertNotNull(updated);
        verify(protocoloRepository, times(1)).save(defaultProtocols);
    }

    @Test
    @DisplayName("deleteById deve deletar e retornar true quando existir")
    void deleteById_ShouldDeleteAndReturnTrue_WhenExists() {
        when(protocoloRepository.existsById(1L)).thenReturn(true);
        doNothing().when(protocoloRepository).deleteById(1L);

        boolean result = protocoloService.deleteById(1L);

        assertTrue(result);
        verify(protocoloRepository, times(1)).existsById(1L);
        verify(protocoloRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById deve retornar false e não deletar quando não existir")
    void deleteById_ShouldReturnFalse_WhenNotExists() {
        when(protocoloRepository.existsById(1L)).thenReturn(false);

        boolean result = protocoloService.deleteById(1L);

        assertFalse(result);
        verify(protocoloRepository, times(1)).existsById(1L);
        verify(protocoloRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteById deve propagar exceção do repository")
    void deleteById_ShouldPropagateException() {
        when(protocoloRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Generic delete error")).when(protocoloRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> protocoloService.deleteById(1L));

        assertEquals("Generic delete error", ex.getMessage());
        verify(protocoloRepository, times(1)).existsById(1L);
        verify(protocoloRepository, times(1)).deleteById(1L);
    }


    private static Protocols prot(Long id) {
        Protocols protocols = new Protocols();
        Usuario usr = new Usuario();

        usr.setId(1L);
        usr.setName("Test");
        usr.setDocument("123456789002");
        usr.setEmail("kwanza@email.com");
        usr.setPassword("123456");
        usr.setRole("default");
        usr.setDate_born(LocalDateTime.parse("2024-06-24T22:32:00"));
        usr.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        usr.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));


        protocols.setId(1L);
        protocols.setUser(usr);
        protocols.setDoc(null);
        protocols.setAdmin(null);
        protocols.setStatus(1L);
        protocols.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        protocols.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        return protocols;
    }
}
