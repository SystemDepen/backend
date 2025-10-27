package com.github.sysdepen.depen_api.Services;

import com.github.sysdepen.depen_api.entity.Admin;
import com.github.sysdepen.depen_api.repository.AdminRepository;
import com.github.sysdepen.depen_api.services.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminServiceTest")
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new Admin();
        admin.setId(1L);
        admin.setName("admin");
        admin.setPassword("password");
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(adminRepository);
    }

    @Test
    @DisplayName("save deve retornar o Admin salvo")
    void save_ShouldReturnSavedAdmin() {
        when(adminRepository.save(admin)).thenReturn(admin);

        Admin saved = adminService.save(admin);

        assertNotNull(saved);
        assertEquals("admin", saved.getName());
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void findAll_ShouldReturnListOfAdmins() {
        List<Admin> admins = Arrays.asList(admin);
        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> result = adminService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia quando não houver registros")
    void findAll_ShouldReturnEmptyListWhenNoAdmins() {
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());

        List<Admin> result = adminService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar Admin quando o ID existir")
    void findById_ShouldReturnAdmin_WhenIdExists() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Optional<Admin> foundAdmin = adminService.findById(1L);

        assertTrue(foundAdmin.isPresent());
        assertEquals("admin", foundAdmin.get().getName());
        verify(adminRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Admin> foundAdmin = adminService.findById(1L);

        assertFalse(foundAdmin.isPresent());
        verify(adminRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("update deve retornar o Admin atualizado")
    void updateAdmin_ShouldReturnUpdatedAdmin() {
        when(adminRepository.save(admin)).thenReturn(admin);

        Admin updatedAdmin = adminService.update(admin);

        assertNotNull(updatedAdmin);
        assertEquals("admin", updatedAdmin.getName());
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    @DisplayName("deleteById deve deletar e retornar true quando existir")
    void deleteById_ShouldDeleteAndReturnTrue_WhenExists() {
        when(adminRepository.existsById(1L)).thenReturn(true);
        doNothing().when(adminRepository).deleteById(1L);

        boolean result = adminService.deleteById(1L);

        assertTrue(result);
        verify(adminRepository, times(1)).existsById(1L);
        verify(adminRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById deve retornar false e não deletar quando não existir")
    void deleteById_ShouldReturnFalse_WhenNotExists() {
        when(adminRepository.existsById(1L)).thenReturn(false);

        boolean result = adminService.deleteById(1L);

        assertFalse(result);
        verify(adminRepository, times(1)).existsById(1L);
        verify(adminRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteById deve propagar exceção do repository")
    void deleteById_ShouldPropagateException() {
        when(adminRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Generic delete error")).when(adminRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.deleteById(1L));

        assertEquals("Generic delete error", ex.getMessage());
        verify(adminRepository, times(1)).existsById(1L);
        verify(adminRepository, times(1)).deleteById(1L);
    }
}