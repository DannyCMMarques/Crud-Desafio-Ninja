package com.crud.demo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.controllers.AcoesControllador;
import com.crud.demo.controllers.PersonagemControllador;
import com.crud.demo.models.DTO.JutsuDTO;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.services.AcoesServiceImpl;
import com.crud.demo.services.PersonagemServiceImpl;
import com.crud.demo.utils.UriLocationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(
    controllers = { PersonagemControllador.class, AcoesControllador.class },
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@Import(UriLocationUtils.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AcoesControlladorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonagemServiceImpl personagemService;

    @MockBean
    private AcoesServiceImpl acoesService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private PersonagemDTO personagemDTO;
    private JutsuDTO jutsu;

    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(inv -> {
            HttpServletRequest req  = inv.getArgument(0);
            HttpServletResponse res = inv.getArgument(1);
            FilterChain chain       = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter).doFilterInternal(any(), any(), any());

        jutsu = JutsuDTO.builder()
            .id(99L)
            .tipo("Ninjutsu")
            .build();

        personagemDTO = PersonagemDTO.builder()
            .id(1L)
            .nome("Naruto Uzumaki")
            .jutsus(List.of(jutsu))
            .build();

        when(personagemService.criarPersonagem(any(PersonagemDTO.class)))
            .thenAnswer(inv -> {
                PersonagemDTO dto = inv.getArgument(0);
                dto.setId(1L);
                return dto;
            });

        when(personagemService.buscarPersonagemPorId(1L))
            .thenReturn(personagemDTO);

        when(acoesService.atacar(1L))
            .thenReturn(personagemDTO.getNome() + " está usando um jutsu de " + jutsu.getTipo() + "!");
        when(acoesService.defender(1L))
            .thenReturn(personagemDTO.getNome() + " está desviando de um ataque utilizando " + jutsu.getTipo() + "!");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve realizar o ataque para um ninja")
    void deveAtacarNinja() throws Exception {
        mockMvc.perform(post("/api/v1/personagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personagemDTO)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/personagens/acoes/{id}/atacar", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
                .value(personagemDTO.getNome() + " está usando um jutsu de " + jutsu.getTipo() + "!"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve realizar a defesa para um ninja")
    void deveDefenderNinja() throws Exception {
        mockMvc.perform(post("/api/v1/personagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personagemDTO)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/personagens/acoes/{id}/defender", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
                .value(personagemDTO.getNome()
                    + " está desviando de um ataque utilizando " + jutsu.getTipo() + "!"));
    }
}
