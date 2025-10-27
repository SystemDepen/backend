package com.github.sysdepen.depen_api.Services;

import com.github.sysdepen.depen_api.security.auth.Usuario;
import com.github.sysdepen.depen_api.security.config.JwtConfig;
import com.github.sysdepen.depen_api.security.config.JwtServiceGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtServiceGeneratorTest {
    private JwtServiceGenerator service;

    private Usuario usuarioMock;
    private String username;
    private String role;
    private Long id;


    @BeforeEach
    void setUp() {
        service = new JwtServiceGenerator();

        // stubs do Usuario
        usuarioMock = Mockito.mock(Usuario.class);
        username = "alice@example.com";
        role = "ADMIN";
        id = 42L;

        when(usuarioMock.getUsername()).thenReturn(username);
        when(usuarioMock.getRole()).thenReturn(role);
        when(usuarioMock.getId()).thenReturn(id);
    }

    private Key signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtConfig.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    void generateToken_deveGerarTokenComClaimsEAssinaturaValida() {
        String token = service.generateToken(usuarioMock);
        assertNotNull(token);
        assertFalse(token.isBlank());

        // extrai algumas infos para garantir que o payload foi populado
        String subject = service.extractUsername(token);
        assertEquals(username, subject);

        // usa extractClaim para recuperar claim customizado
        String extractedRole = service.extractClaim(token, claims -> claims.get("role", String.class));
        String extractedId = service.extractClaim(token, claims -> claims.get("id", String.class));
        assertEquals(role, extractedRole);
        assertEquals(String.valueOf(id), extractedId);

        // data de expiração deve ser no futuro
        Date exp = service.extractClaim(token, Claims::getExpiration);
        assertTrue(exp.after(new Date()));
    }

    @Test
    void isTokenValid_deveRetornarTrue_quandoUsuarioConfereEtokenNaoExpirou() {
        String token = service.generateToken(usuarioMock);
        UserDetails details = User.withUsername(username).password("x").authorities("ROLE_USER").build();

        assertTrue(service.isTokenValid(token, details));
    }

    @Test
    void isTokenValid_deveRetornarFalse_quandoUsuarioNaoConfere() {
        String token = service.generateToken(usuarioMock);
        UserDetails outro = User.withUsername("outro@x.com").password("y").authorities("ROLE_USER").build();

        assertFalse(service.isTokenValid(token, outro));
    }

    @Test
    void isTokenValid_deveLancarExpiredJwtException_quandoTokenExpirado() {
        String expired = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 3_600_000))
                .setExpiration(new Date(System.currentTimeMillis() - 1_000)) // já expirado
                .signWith(signingKey(), JwtConfig.ALGORITMO_ASSINATURA)
                .compact();

        UserDetails details = User.withUsername(username).password("x").authorities("ROLE_USER").build();

        // a validação vai tentar extrair claims e o parser lançará ExpiredJwtException
        assertThrows(ExpiredJwtException.class, () -> service.isTokenValid(expired, details));

        // Não chame service.extractClaim(expired, ...) aqui — também lançaria ExpiredJwtException.
    }
}
