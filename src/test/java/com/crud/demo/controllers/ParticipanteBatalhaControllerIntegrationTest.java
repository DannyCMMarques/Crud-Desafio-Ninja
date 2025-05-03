package com.crud.demo.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaRequestDTO;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaResponseDTO;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.services.contratos.ParticipanteBatalhaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;

@WebMvcTest(controllers = ParticipanteBatalhaController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
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

        private ParticipanteBatalhaRequestDTO participanteRequestDTO;
        private ParticipanteBatalhaResponseDTO participanteResponseDTO;

        @BeforeEach
        void setup() throws Exception {
                doAnswer(inv -> {
                        FilterChain chain = inv.getArgument(2);
                        chain.doFilter(inv.getArgument(0), inv.getArgument(1));
                        return null;
                }).when(jwtAuthenticationFilter).doFilterInternal(any(), any(), any());

                participanteRequestDTO = new ParticipanteBatalhaRequestDTO(
                                1L, "naruto", "Kakashi Hatake", 1, false);

                participanteResponseDTO = ParticipanteBatalhaResponseDTO.builder()
                                .id(1L).nomeUsuario("naruto")
                                .personagem("Kakashi Hatake")
                                .playerOrder(1).vencedor(false)
                                .build();

                Page<ParticipanteBatalhaResponseDTO> page = new PageImpl<>(List.of(participanteResponseDTO));

                when(participanteService.listarTodosParticipantes(
                                anyString(), anyString(), anyInt(), anyInt()))
                                .thenReturn(page);

                when(participanteService.getParticipanteBatalhaById(1L))
                                .thenReturn(participanteResponseDTO);
                when(participanteService.atualizarParticipanteBatalha(1L, participanteRequestDTO))
                                .thenReturn(participanteResponseDTO);
                when(participanteService.criarParticipanteBatalha(participanteRequestDTO))
                                .thenReturn(participanteResponseDTO);
        }

        @Test
        @DisplayName("Deve criar participante e retornar 201 com header Location")
        @WithMockUser(roles = "USER")
        void DeveCriarParticipanteBatalhaComSucesso() throws Exception {
                mockMvc.perform(post("/api/v1/participante-batalha")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(participanteRequestDTO)))
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
                                .content(objectMapper.writeValueAsString(participanteRequestDTO)))
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
        @DisplayName("Deve retornar lista de participantes com sucesso")
        @WithMockUser(roles = "USER")
        void deveRetornarParticipantes() throws Exception {
                mockMvc.perform(get("/api/v1/participante-batalha")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1));
        }

}
