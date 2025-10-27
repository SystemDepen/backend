package com.github.sysdepen.depen_api.Services;


import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.entity.Protocols;
import com.github.sysdepen.depen_api.repository.AdminRepository;
import com.github.sysdepen.depen_api.repository.UsuarioRepository;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.services.AdminService;
import com.github.sysdepen.depen_api.services.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceTest")
public class UserServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario defaultUsers;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    void setUp() {
        defaultUsers = usr(1L);
    }


    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void save_deveCodificarSenhaPersistirEDevolverMensagem() {
        Usuario usuario = new Usuario();
        usuario.setPassword("senhaPura");

        when(bCryptPasswordEncoder.encode("senhaPura")).thenReturn("senhaHash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        String msg = usuarioService.save(usuario);

        assertEquals("Usuario cadastrado com sucesso", msg);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(1)).save(captor.capture());
        Usuario salvo = captor.getValue();

        verify(bCryptPasswordEncoder, times(1)).encode("senhaPura");
        assertEquals("senhaHash", salvo.getPassword());
    }

    @Test
    void save_devePropagarExcecaoSeRepositorioFalhar() {
        // arrange
        Usuario usuario = new Usuario();
        usuario.setPassword("abc");

        when(bCryptPasswordEncoder.encode("abc")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenThrow(new RuntimeException("falha de banco"));

        // act + assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.save(usuario));
        assertTrue(ex.getMessage().contains("falha"));
    }

    @Test
    @DisplayName("findAll deve retornar lista com registros")
    void findAll_ShouldReturnList() {
        when(usuarioRepository.findAll()).thenReturn(List.of(defaultUsers));

        List<Usuario> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia")
    void findAll_ShouldReturnEmptyList() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> result = usuarioService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar Optional com entidade quando existir")
    void findById_ShouldReturnWhenExists() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(defaultUsers));

        Usuario found = usuarioService.findById(1L);

        Optional<Usuario> existUsr = Optional.ofNullable(found);

        assertTrue(existUsr.isPresent());
        assertEquals(defaultUsers, existUsr.get());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve retornar Optional.empty quando não existir")
    void findById_ShouldReturnEmptyWhenNotExists() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Usuario found = usuarioService.findById(1L);

        Optional<Usuario> existUsr = Optional.ofNullable(found);

        assertTrue(existUsr.isEmpty());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("update deve salvar e retornar a entidade atualizada")
    void update_ShouldReturnUpdatedEntity() {
        when(usuarioRepository.save(defaultUsers)).thenReturn(defaultUsers);

        String updated = usuarioService.update(defaultUsers, defaultUsers.getId());

        assertNotNull(updated);
        verify(usuarioRepository, times(1)).save(defaultUsers);
    }

    @Test
    @DisplayName("deleteById deve deletar e retornar true quando existir")
    void deleteById_ShouldDeleteAndReturnTrue_WhenExists() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        boolean result = usuarioService.delete(1L);

        assertTrue(result);
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById deve retornar false e não deletar quando não existir")
    void deleteById_ShouldReturnFalse_WhenNotExists() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        boolean result = usuarioService.delete(1L);

        assertFalse(result);
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteById deve propagar exceção do repository")
    void deleteById_ShouldPropagateException() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Generic delete error")).when(usuarioRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.delete(1L));

        assertEquals("Generic delete error", ex.getMessage());
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    private static Usuario usr(Long id) {
        Usuario usuario =  new Usuario();
        usuario.setId(1L);
        usuario.setName("Test");
        usuario.setDocument("123456789002");
        usuario.setEmail("kwanza@email.com");
        usuario.setPassword("123456");
        usuario.setRole("default");
        usuario.setDate_born(LocalDateTime.parse("2024-06-24T22:32:00"));
        usuario.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        usuario.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        return usuario;
    }
}
