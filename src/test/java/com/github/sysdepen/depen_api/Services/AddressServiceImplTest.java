package com.github.sysdepen.depen_api.Services;

import com.github.sysdepen.depen_api.entity.Address;
import com.github.sysdepen.depen_api.repository.AddressRepository;
import com.github.sysdepen.depen_api.services.AddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    private Address buildAddress() {
        Address a = new Address();
        a.setId(1L);
        a.setStreet("Main Street");
        a.setCity("Cityville");
        return a;
    }

    @Test
    @DisplayName("save: deve retornar o endereço salvo")
    void save_shouldReturnSavedAddress() {
        // Arrange
        Address address = buildAddress();
        given(addressRepository.save(address)).willReturn(address);

        // Act
        Address result = addressService.save(address);

        // Assert
        assertNotNull(result);
        assertEquals("Main Street", result.getStreet());
        then(addressRepository).should(times(1)).save(address);
        then(addressRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("findAll: deve retornar a lista de endereços")
    void findAll_shouldReturnListOfAddresses() {
        // Arrange
        Address address = buildAddress();
        given(addressRepository.findAll()).willReturn(List.of(address));

        // Act
        List<Address> result = addressService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Main Street", result.get(0).getStreet());
        then(addressRepository).should(times(1)).findAll();
        then(addressRepository).shouldHaveNoMoreInteractions();
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("deve retornar Optional preenchido quando existir")
        void shouldReturnAddressWhenIdExists() {
            // Arrange
            Address address = buildAddress();
            given(addressRepository.findById(1L)).willReturn(Optional.of(address));

            // Act
            Optional<Address> result = addressService.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals("Main Street", result.get().getStreet());
            then(addressRepository).should(times(1)).findById(1L);
            then(addressRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("deve retornar Optional vazio quando não existir")
        void shouldReturnEmptyWhenIdDoesNotExist() {
            // Arrange
            given(addressRepository.findById(1L)).willReturn(Optional.empty());

            // Act
            Optional<Address> result = addressService.findById(1L);

            // Assert
            assertTrue(result.isEmpty());
            then(addressRepository).should(times(1)).findById(1L);
            then(addressRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Test
    @DisplayName("update: deve retornar o endereço atualizado (save no repositório)")
    void update_shouldReturnUpdatedAddress() {
        // Arrange
        Address address = buildAddress();
        given(addressRepository.save(address)).willReturn(address);

        // Act
        Address result = addressService.update(address);

        // Assert
        assertNotNull(result);
        assertEquals("Main Street", result.getStreet());
        then(addressRepository).should(times(1)).save(address);
        then(addressRepository).shouldHaveNoMoreInteractions();
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("deve deletar e retornar true quando existir")
        void shouldDeleteAndReturnTrueWhenExists() {
            // Arrange
            long id = 1L;
            given(addressRepository.existsById(id)).willReturn(true);
            willDoNothing().given(addressRepository).deleteById(id);

            // Act
            boolean deleted = addressService.deleteById(id);

            // Assert
            assertTrue(deleted);
            then(addressRepository).should(times(1)).existsById(id);
            then(addressRepository).should(times(1)).deleteById(id);
            then(addressRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("deve retornar false e não chamar delete quando não existir")
        void shouldReturnFalseAndNotDeleteWhenDoesNotExist() {
            // Arrange
            long id = 1L;
            given(addressRepository.existsById(id)).willReturn(false);

            // Act
            boolean deleted = addressService.deleteById(id);

            // Assert
            assertFalse(deleted);
            then(addressRepository).should(times(1)).existsById(id);
            then(addressRepository).should(never()).deleteById(anyLong());
            then(addressRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Test
    @DisplayName("findAll: deve lidar com lista vazia")
    void findAll_shouldHandleEmptyList() {
        // Arrange
        given(addressRepository.findAll()).willReturn(Collections.emptyList());

        // Act
        List<Address> result = addressService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressRepository, times(1)).findAll();
        verifyNoMoreInteractions(addressRepository);
    }
}

