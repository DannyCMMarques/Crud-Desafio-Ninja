package com.crud.demo.security;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.crud.demo.Exceptions.usuarioException.UsuarioNaoAutorizadoException;
import com.crud.demo.security.Service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {


    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;


    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve prosseguir sem autenticação quando não houver header Authorization")
    void deveProsseguirSemAutenticacaoQuandoSemHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Deve prosseguir sem autenticação quando o header Authorization for inválido")
    void deveProsseguirSemAutenticacaoQuandoHeaderInvalido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Invalid header");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Deve autenticar corretamente quando o token for válido")
    void deveAutenticarQuandoTokenValido() throws ServletException, IOException {
        String token = "valid.token.here";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extrairUsername(token)).thenReturn("user@example.com");
        UserDetails userDetails = User.withUsername("user@example.com").password("pass").roles("USER").build();
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValido(token, userDetails)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extrairUsername(token);
        verify(userDetailsService).loadUserByUsername("user@example.com");
        verify(jwtService).isTokenValido(token, userDetails);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(userDetails, auth.getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve não autenticar quando o token for inválido")
    void deveNaoAutenticarQuandoTokenInvalido() throws ServletException, IOException {
        String token = "invalid.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extrairUsername(token)).thenReturn("user@example.com");
        UserDetails userDetails = User.withUsername("user@example.com").password("pass").roles("USER").build();
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValido(token, userDetails)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtService).extrairUsername(token);
        verify(userDetailsService).loadUserByUsername("user@example.com");
        verify(jwtService).isTokenValido(token, userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoAutorizadoException quando ocorrer erro ao processar token")
    void deveLancarUsuarioNaoAutorizadoQuandoErroNoProcessamentoDoToken() throws IOException, ServletException {
        String token = "bad.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extrairUsername(token)).thenThrow(new RuntimeException("Parsing error"));

        assertThrows(UsuarioNaoAutorizadoException.class, () ->
            filter.doFilterInternal(request, response, filterChain)
        );
        verify(filterChain, never()).doFilter(request, response);
    }
}
