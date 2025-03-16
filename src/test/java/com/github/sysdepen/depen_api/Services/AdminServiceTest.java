//package com.github.sysdepen.depen_api.Services;
//
//import com.github.sysdepen.depen_api.entity.Admin;
//import com.github.sysdepen.depen_api.repository.AdminRepository;
//import com.github.sysdepen.depen_api.services.AdminService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class AdminServiceTest {
//
//    @InjectMocks
//    private AdminService adminService;
//
//    @Mock
//    private AdminRepository adminRepository;
//
//    private Admin admin;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        admin = new Admin();
//        admin.setId(1L);
//        admin.setName("admin");
//        admin.setPassword("password");
//    }
//
//    @Test
//    void saveAdmin_ShouldReturnSavedAdmin() {
//        when(adminRepository.save(admin)).thenReturn(admin);
//
//        Admin savedAdmin = adminService.save(admin);
//
//        assertNotNull(savedAdmin);
//        assertEquals("admin", savedAdmin.getName());
//        verify(adminRepository, times(1)).save(admin);
//    }
//
//    @Test
//    void findAll_ShouldReturnListOfAdmins() {
//        List<Admin> admins = Arrays.asList(admin);
//        when(adminRepository.findAll()).thenReturn(admins);
//
//        List<Admin> result = adminService.findAll();
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        verify(adminRepository, times(1)).findAll();
//    }
//
//    @Test
//    void findById_ShouldReturnAdmin_WhenIdExists() {
//        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
//
//        Optional<Admin> foundAdmin = adminService.findById(1L);
//
//        assertTrue(foundAdmin.isPresent());
//        assertEquals("admin", foundAdmin.get().getName());
//        verify(adminRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
//        when(adminRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Optional<Admin> foundAdmin = adminService.findById(1L);
//
//        assertFalse(foundAdmin.isPresent());
//        verify(adminRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void updateAdmin_ShouldReturnUpdatedAdmin() {
//        when(adminRepository.save(admin)).thenReturn(admin);
//
//        Admin updatedAdmin = adminService.update(admin);
//
//        assertNotNull(updatedAdmin);
//        assertEquals("admin", updatedAdmin.getName());
//        verify(adminRepository, times(1)).save(admin);
//    }
//
//    @Test
//    void deleteById_ShouldCallDeleteOnRepository() {
//        doNothing().when(adminRepository).deleteById(1L);
//
//        adminService.deleteById(1L);
//
//        verify(adminRepository, times(1)).deleteById(1L);
//    }
//}