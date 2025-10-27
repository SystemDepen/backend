package com.github.sysdepen.depen_api.Exceptions;


import com.github.sysdepen.depen_api.exceptions.GlobalExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private MethodArgumentNotValidException manve;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequestWithFieldErrors() {
        // Arrange: cria dois FieldError e configura o mock do BindingResult
        FieldError f1 = new FieldError("obj", "nome", "obrigatório");
        FieldError f2 = new FieldError("obj", "idade", "mínimo 18");
        List<org.springframework.validation.ObjectError> allErrors = Arrays.asList(f1, f2);

        when(bindingResult.getAllErrors()).thenReturn(allErrors);
        when(manve.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<Map<String, String>> resp = handler.handleValidationExceptions(manve);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("obrigatório", resp.getBody().get("nome"));
        assertEquals("mínimo 18", resp.getBody().get("idade"));

        // Verifica interações (opcional)
        verify(manve, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getAllErrors();
    }

    @Test
    void handleHttpMessageNotReadableException_shouldReturnBadRequest() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("malformado");
        ResponseEntity<String> resp = handler.handleHttpMessageNotReadableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Formato do JSON inválido ou malformado.", resp.getBody());
    }

    @Test
    void handleEntityNotFoundException_shouldReturnNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("nao achei");
        ResponseEntity<String> resp = handler.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals("Recurso não encontrado.", resp.getBody());
    }

    @Test
    void handleGlobalExceptions_shouldReturnInternalServerErrorWithMessage() {
        Exception ex = new Exception("boom");
        ResponseEntity<String> resp = handler.handleGlobalExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Erro inesperado: boom"));
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequestWithMessage() {
        IllegalArgumentException ex = new IllegalArgumentException("param inválido");
        ResponseEntity<String> resp = handler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Argumento inválido: param inválido", resp.getBody());
    }

    @Test
    void handleAuthenticationException_shouldReturnUnauthorizedWithBodyMap() throws Exception {
        AuthenticationException ex = new AuthenticationException("fail");
        ResponseEntity<Map<String, String>> resp = handler.handleAuthenticationException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        Map<String, String> expected = new HashMap<>();
        expected.put("error", "Usuário ou senha incorretos.");
        assertEquals(expected, resp.getBody());
    }
}
