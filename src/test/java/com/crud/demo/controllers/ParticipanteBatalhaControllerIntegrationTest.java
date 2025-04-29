package com.crud.demo.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.models.DTO.ParticipanteBatalhaDTO;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.services.contratos.ParticipanteBatalhaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(
        controllers = ParticipanteBatalhaController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ParticipanteBatalhaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private ParticipanteBatalhaService participanteService;

    private ParticipanteBatalhaDTO participanteDTO;

    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(inv -> {
            HttpServletRequest req = inv.getArgument(0);
            HttpServletResponse res = inv.getArgument(1);
            FilterChain chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter).doFilterInternal(any(), any(), any());

        participanteDTO = ParticipanteBatalhaDTO.builder()
                .id(1L)
                .nomeUsuario("naruto")
                .playerOrder(1)
                .vencedor(false)
                .build();

        when(participanteService.criarParticipanteBatalha(any(ParticipanteBatalhaDTO.class)))
                .thenReturn(participanteDTO);
        when(participanteService.getParticipanteBatalhaById(1L))
                .thenReturn(participanteDTO);
        when(participanteService.atualizarParticipanteBatalha(eq(1L), any(ParticipanteBatalhaDTO.class)))
                .thenReturn(participanteDTO);
    }

    @Test
    @DisplayName("Deve criar participante e retornar 201 com header Location")
    @WithMockUser(roles = "USER")
    void DeveCriarParticipanteBatalhaComSucesso() throws Exception {
        participanteDTO.setId(1L);
        mockMvc.perform(post("/api/v1/participante-batalha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(participanteDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Deve buscar participante por ID e retornar JSON com sucesso")
    @WithMockUser(roles = "USER")
    void deveBuscarParticipantePorId() throws Exception {
        mockMvc.perform(get("/api/v1/participante-batalha/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeUsuario").value("naruto"));
    }

    @Test
    @DisplayName("Deve atualizar participante e retornar 200")
    @WithMockUser(roles = "USER")
    void deveAtualizarParticipante() throws Exception {
        mockMvc.perform(put("/api/v1/participante-batalha/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(participanteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Deve deletar participante e retornar 204")
    @WithMockUser(roles = "USER")
    void deveDeletarParticipante() throws Exception {
        doNothing().when(participanteService).deletarParticipanteBatalha(1L);

        mockMvc.perform(delete("/api/v1/participante-batalha/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar participantes e retornar lista com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarParticipantes() throws Exception {
        Page<ParticipanteBatalhaDTO> page = new PageImpl<>(List.of(participanteDTO));
        when(participanteService.listarTodosParticipantes(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/participante-batalha")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}
