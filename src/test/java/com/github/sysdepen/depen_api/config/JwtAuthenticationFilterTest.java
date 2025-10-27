package com.github.sysdepen.depen_api.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.github.sysdepen.depen_api.security.config.JwtAuthenticationFilter;
import com.github.sysdepen.depen_api.security.config.JwtServiceGenerator;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    JwtServiceGenerator jwtService;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    FilterChain filterChain;

    @InjectMocks
    JwtAuthenticationFilter filter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void semAuthorizationHeader_deveSeguirCadeiaSemAutenticar() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void tokenInvalido_deveSeguirCadeiaSemAutenticar() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer abc.xyz.123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("abc.xyz.123")).thenReturn("user@example.com");
        when(jwtService.isTokenValid(eq("abc.xyz.123"), any(UserDetails.class))).thenReturn(false);

        UserDetails ud = new User("user@example.com", "pwd",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(ud);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void tokenValido_deveAutenticarEChamarCadeia() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid.token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("valid.token")).thenReturn("user@example.com");

        UserDetails ud = new User("user@example.com", "pwd",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(ud);
        when(jwtService.isTokenValid("valid.token", ud)).thenReturn(true);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getPrincipal()).isEqualTo(ud);
        assertThat(auth.isAuthenticated()).isTrue();
    }

    @Test
    void jaAutenticado_noContexto_naoReautentica() throws Exception {
        UserDetails ud = new User("alguem@ex.com", "pwd",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        var preAuth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(preAuth);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer qualquer.coisa");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("qualquer.coisa")).thenReturn("user@example.com");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(userDetailsService);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(preAuth);
    }
}