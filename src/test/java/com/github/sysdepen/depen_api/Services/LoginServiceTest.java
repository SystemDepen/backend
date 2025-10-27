package com.github.sysdepen.depen_api.Services;

import com.github.sysdepen.depen_api.security.auth.Login;
import com.github.sysdepen.depen_api.security.auth.LoginRepository;
import com.github.sysdepen.depen_api.security.auth.LoginService;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.security.config.JwtServiceGenerator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginServiceTest")
public class LoginServiceTest {
    @InjectMocks
    private LoginService loginService;

    @Mock
    private LoginRepository repository;

    @Mock
    private JwtServiceGenerator jwtService;

    @Mock
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    private Usuario usuario;
    private Login loginPayload;


    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName("Test");
        usuario.setDocument("12345678900");
        usuario.setEmail("fulano@ex.com");
        usuario.setPassword("Segredo123");
        usuario.setRole("default");
        usuario.setDate_born(LocalDateTime.parse("2024-06-24T22:32:00"));
        usuario.setCreated_at(LocalDateTime.parse("2024-06-24T22:32:00"));
        usuario.setUpdated_at(LocalDateTime.parse("2024-06-24T22:32:00"));

        loginPayload = new Login();
        loginPayload.setDocument(usuario.getDocument());
        loginPayload.setPassword("Segredo123");
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, jwtService, authenticationManager);
    }

    @Test
    @DisplayName("logar: deve autenticar e retornar token (200 OK)")
    void logar_deveRetornarToken_quandoCredenciaisValidas() {
        // authenticate() é chamado, retorno não é usado pelo service
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(repository.findByDocument(usuario.getDocument())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("fake.jwt.token");

        String token = loginService.logar(loginPayload);

        assertEquals("fake.jwt.token", token);

        // Verifica ordem: autentica -> busca usuário -> gera token
        InOrder inOrder = inOrder(authenticationManager, repository, jwtService);
        inOrder.verify(authenticationManager).authenticate(any());
        inOrder.verify(repository).findByDocument(usuario.getDocument());
        inOrder.verify(jwtService).generateToken(usuario);

        // Verifica que o UsernamePasswordAuthenticationToken foi montado com os dados corretos
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        UsernamePasswordAuthenticationToken authToken = captor.getValue();
        assertEquals(loginPayload.getDocument(), authToken.getPrincipal());
        assertEquals(loginPayload.getPassword(), authToken.getCredentials());
    }

    @Test
    @DisplayName("logar: deve lançar AuthenticationException (401) quando credenciais inválidas")
    void logar_deveLancarAuthentication_quandoCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad"));

        AuthenticationException ex =
                assertThrows(AuthenticationException.class, () -> loginService.logar(loginPayload));

        assertEquals("Usuário ou senha incorretos.", ex.getMessage());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(repository, never()).findByDocument(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("logar: deve lançar AuthenticationException (400 genérico do controller) quando há falha de autenticação")
    void logar_deveLancarAuthentication_quandoFalhaGenericaAutenticacao() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new InternalAuthenticationServiceException("provider down"));

        AuthenticationException ex =
                assertThrows(AuthenticationException.class, () -> loginService.logar(loginPayload));

        assertEquals("Falha na autenticação.", ex.getMessage());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(repository, never()).findByDocument(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("logar: deve lançar NoSuchElementException quando usuário não encontrado após autenticar")
    void logar_deveLancarNoSuchElement_quandoUsuarioNaoEncontrado() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(repository.findByDocument(loginPayload.getDocument())).thenReturn(Optional.empty());

        NoSuchElementException ex =
                assertThrows(NoSuchElementException.class, () -> loginService.logar(loginPayload));
        assertNotNull(ex);

        verify(authenticationManager, times(1)).authenticate(any());
        verify(repository, times(1)).findByDocument(loginPayload.getDocument());
        verify(jwtService, never()).generateToken(any());
    }
}
