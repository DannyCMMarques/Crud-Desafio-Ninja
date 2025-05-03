package com.crud.demo.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.Exceptions.usuarioException.UsuarioNaoAutorizadoException;
import com.crud.demo.models.DTO.login.LoginDTO;
import com.crud.demo.models.DTO.login.LoginResponseDTO;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.services.LoginServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(controllers = LoginController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginServiceImpl loginService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private String token;
    private LoginResponseDTO loginResponseDTO;
    private LoginDTO loginRequestDTO;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        doAnswer(inv -> {
            HttpServletRequest req = inv.getArgument(0);
            HttpServletResponse res = inv.getArgument(1);
            FilterChain chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter)
                .doFilterInternal(any(), any(), any());

        loginRequestDTO = new LoginDTO("user@example.com", "senha123");

        token = "jwt-token-123";

        loginResponseDTO = LoginResponseDTO.builder()
                .token(token)
                .sub("user@example.com")
                .role("USER")
                .createdAt("2025-04-21T18:00:00Z")
                .exp(new Date(1_650_000_000_000L))
                .iat(1_650_000_000L)
                .build();

        when(loginService.autentificar(any(LoginDTO.class))).thenReturn(token);
        when(loginService.gerarLoginResponse(token)).thenReturn(loginResponseDTO);

    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void deveRealizarLoginComSucesso() throws Exception {
        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.sub").value("user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.createdAt").value("2025-04-21T18:00:00Z"))
                .andExpect(jsonPath("$.exp").exists())
                .andExpect(jsonPath("$.iat").value(loginResponseDTO.getIat()));

        verify(loginService).autentificar(any(LoginDTO.class));
        verify(loginService).gerarLoginResponse(token);
    }

    @Test
    @DisplayName("Deve retornar Bad Request quando credenciais inválidas")
    void deveRetornarBadRequestQuandoCredenciaisInvalidas() throws Exception {

        LoginDTO loginRequestWrong = new LoginDTO("user@example.com", "wrongpass");

        when(loginService.autentificar(loginRequestWrong)).thenThrow(new UsuarioNaoAutorizadoException());

        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestWrong)))
                .andExpect(status().isBadRequest());

        verify(loginService).autentificar(any(LoginDTO.class));
        verify(loginService, never()).gerarLoginResponse(anyString());
    }
}
