package com.crud.demo.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.controllers.PersonagemControllador;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.services.PersonagemServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(
    controllers = PersonagemControllador.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PersonagemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private PersonagemServiceImpl personagemService;

    @MockBean
    private PersonagemMapper personagemMapper;

    private PersonagemDTO personagemDTO;

    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(inv -> {
            HttpServletRequest req  = inv.getArgument(0);
            HttpServletResponse res = inv.getArgument(1);
            FilterChain chain       = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter).doFilterInternal(any(), any(), any());

        personagemDTO = new PersonagemDTO();
        personagemDTO.setId(1L);
        personagemDTO.setNome("Naruto");
        personagemDTO.setIdade(33L);
        personagemDTO.setAldeia("Konoha");
        personagemDTO.setChakra(1000L);
        personagemDTO.setJutsus(Collections.emptyList());

        when(personagemMapper.toEntity(any(PersonagemDTO.class)))
            .thenReturn(new Personagem());
        when(personagemMapper.toDto(any(Personagem.class)))
            .thenReturn(personagemDTO);
    }

    @Test
    @DisplayName("Deve criar personagem e retornar 201 com header Location")
    @WithMockUser(roles = "USER")
    void deveCriarPersonagem() throws Exception {
        when(personagemService.criarPersonagem(any(PersonagemDTO.class)))
            .thenReturn(personagemDTO);

        mockMvc.perform(post("/api/v1/personagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personagemDTO)))
            .andExpect(status().isCreated())
            .andExpect(header().string(
                HttpHeaders.LOCATION,
                endsWith("/api/v1/personagens/1")));
    }

    @Test
    @DisplayName("Deve buscar personagem por ID e retornar JSON com sucesso")
    @WithMockUser(roles = "USER")
    void deveBuscarPersonagemPorId() throws Exception {
        when(personagemService.buscarPersonagemPorId(1L))
            .thenReturn(personagemDTO);

        mockMvc.perform(get("/api/v1/personagens/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Naruto"));
    }

    @Test
    @DisplayName("Deve deletar personagem e retornar 204")
    @WithMockUser(roles = "USER")
    void deveDeletarPersonagem() throws Exception {
        doAnswer(inv -> null).when(personagemService).deletarPersonagem(1L);

        mockMvc.perform(delete("/api/v1/personagens/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar todos os personagens e retornar lista com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarTodosOsPersonagens() throws Exception {
        Page<PersonagemDTO> page = new PageImpl<>(List.of(personagemDTO));
        when(personagemService.filtrarPersonagens(
            any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/v1/personagens")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1));
    }
}
