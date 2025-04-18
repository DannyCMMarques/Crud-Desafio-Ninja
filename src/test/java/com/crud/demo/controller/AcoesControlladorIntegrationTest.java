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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AcoesControlladorIntegrationTest {

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
        @DisplayName("Deve realizar o ataque para um ninja")
        void deveAtacarNinja() throws Exception {
                String json = objectMapper.writeValueAsString(personagemDTO);

                MvcResult result = mockMvc.perform(post("/api/v1/personagens")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated())
                                .andReturn();

                PersonagemDTO personagemCriado = objectMapper.readValue(result.getResponse().getContentAsString(),
                                PersonagemDTO.class);

                mockMvc.perform(get("/api/v1/personagens/acoes/" + personagemCriado.getId() + "/atacar"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value(
                                                personagemCriado.getNome() + " está usando um jutsu de Ninjutsu!"));
        }

        // @Test
        // @DisplayName("Deve realizar a defesa para um ninja")
        // void deveDefenderNinja() throws Exception {
        //         String json = objectMapper.writeValueAsString(personagemDTO);

        //         MvcResult result = mockMvc.perform(post("/api/v1/personagens")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content(json))
        //                         .andExpect(status().isCreated())
        //                         .andReturn();

        //         PersonagemDTO personagemCriado = objectMapper.readValue(result.getResponse().getContentAsString(),
        //                         PersonagemDTO.class);

        //         mockMvc.perform(get("/api/v1/personagens/acoes/" + personagemCriado.getId() + "/defender"))
        //                         .andExpect(status().isOk())
        //                         .andExpect(jsonPath("$.message").value(personagemCriado.getNome()
        //                                         + " está desviando de um ataque utilizando Ninjutsu!"));
        // }

}
