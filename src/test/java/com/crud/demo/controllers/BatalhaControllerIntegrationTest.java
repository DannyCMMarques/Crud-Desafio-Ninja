package com.crud.demo.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.models.DTO.batalha.BatalhaRequestDTO;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.models.enuns.StatusEnum;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.services.BatalhaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(controllers = BatalhaController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class BatalhaControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        private BatalhaServiceImpl batalhaService;

        private BatalhaResponseDTO batalhaDTO;
        private BatalhaRequestDTO batalhaRequest;

        @BeforeEach
        void setup() throws ServletException, java.io.IOException {
                doAnswer(inv -> {
                        HttpServletRequest req = inv.getArgument(0);
                        HttpServletResponse res = inv.getArgument(1);
                        FilterChain chain = inv.getArgument(2);
                        chain.doFilter(req, res);
                        return null;
                }).when(jwtAuthenticationFilter).doFilterInternal(any(), any(), any());

                batalhaDTO = BatalhaResponseDTO.builder()
                                .id(1L)
                                .criadoEm(LocalDateTime.now())
                                .status(StatusEnum.NAO_INICIADA)
                                .participantesBatalha(List.of())
                                .build();

                batalhaRequest = new BatalhaRequestDTO();
                batalhaRequest.setFinalizadoEm(null);
                batalhaRequest.setStatus(StatusEnum.NAO_INICIADA);
                when(batalhaService.criarBatalha(any(BatalhaRequestDTO.class)))
                                .thenReturn(batalhaDTO);
                when(batalhaService.getBatalhaById(1L))
                                .thenReturn(batalhaDTO);
                when(batalhaService.atualizarBatalha(eq(1L), any(BatalhaResponseDTO.class)))
                                .thenReturn(batalhaDTO);
        }

        @Test
        @DisplayName("Deve criar batalha e retornar 201 com header Location")
        @WithMockUser(roles = "USER")
        void deveCriarBatalha() throws Exception {
                mockMvc.perform(post("/api/v1/batalha")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(batalhaRequest)))
                                .andExpect(status().isCreated());

        }

        @Test
        @DisplayName("Deve buscar batalha por ID e retornar JSON com sucesso")
        @WithMockUser(roles = "USER")
        void deveBuscarBatalhaPorId() throws Exception {
                mockMvc.perform(get("/api/v1/batalha/{id}", 1L))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("NAO_INICIADA"));
        }

        @Test
        @DisplayName("Deve atualizar batalha e retornar 200")
        @WithMockUser(roles = "USER")
        void deveAtualizarBatalha() throws Exception {
                mockMvc.perform(put("/api/v1/batalha/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(batalhaDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        @DisplayName("Deve deletar batalha e retornar 204")
        @WithMockUser(roles = "USER")
        void deveDeletarBatalha() throws Exception {
                doNothing().when(batalhaService).deletarBatalha(1L);

                mockMvc.perform(delete("/api/v1/batalha/{id}", 1L))
                                .andExpect(status().isNoContent());
        }

}
