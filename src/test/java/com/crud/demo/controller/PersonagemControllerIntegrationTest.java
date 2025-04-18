package com.crud.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.utils.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PersonagemControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private PersonagemDTO personagemDTO;

        @BeforeEach
        public void setup() {
                personagemDTO = TestDataFactory.criarPersonagemDTOComNinjutsu();
        }

        @Test
        @DisplayName("Deve criar corretamente um personagem com jutsu associado")
        void deveCriarPersonagem() throws Exception {
                String json = objectMapper.writeValueAsString(personagemDTO);

                mockMvc.perform(post("/api/v1/personagens")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.nome").value(TestDataFactory.NOME_PADRAO))
                                .andExpect(jsonPath("$.jutsus.length()").value(1));
        }

        @Test
        @DisplayName("Deve buscar um personagem pelo ID após criação")
        void deveBuscarPersonagemPorId() throws Exception {
                String json = objectMapper.writeValueAsString(personagemDTO);

                MvcResult result = mockMvc.perform(post("/api/v1/personagens")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated())
                                .andReturn();

                PersonagemDTO personagemCriado = objectMapper.readValue(result.getResponse().getContentAsString(),
                                PersonagemDTO.class);

                mockMvc.perform(get("/api/v1/personagens/" + personagemCriado.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value(TestDataFactory.NOME_PADRAO))
                                .andExpect(jsonPath("$.jutsus.length()").value(1));
        }

        @Test
        @DisplayName("Deve deletar um personagem corretamente")
        void deveDeletarPersonagem() throws Exception {
                String json = objectMapper.writeValueAsString(personagemDTO);

                MvcResult result = mockMvc.perform(post("/api/v1/personagens")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated())
                                .andReturn();

                PersonagemDTO personagemCriado = objectMapper.readValue(result.getResponse().getContentAsString(),
                                PersonagemDTO.class);

                mockMvc.perform(delete("/api/v1/personagens/" + personagemCriado.getId()))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/v1/personagens/" + personagemCriado.getId()))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Deve listar todos os personagens")
        void deveListarTodosOsPersonagens() throws Exception {
                String json = objectMapper.writeValueAsString(personagemDTO);

                mockMvc.perform(post("/api/v1/personagens")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated());

                mockMvc.perform(get("/api/v1/personagens"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1));
        }
}
