package com.crud.demo.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PersonagemTest {

    private Personagem personagem;

    @BeforeEach
    void setUp() {
        personagem = Personagem.builder()
                .id(123L)
                .nome("Naruto Uzumaki")
                .idade(16L)
                .aldeia("Konoha")
                .build();
    }

    @Test
    @DisplayName("Deve construir Personagem com campos populados e valores padrão")
    void deveConstruirPersonagemComCamposPopuladosEPadroes() {
        assertEquals(123L, personagem.getId());
        assertEquals("Naruto Uzumaki", personagem.getNome());
        assertEquals(16L, personagem.getIdade());
        assertEquals("Konoha", personagem.getAldeia());

        assertNotNull(personagem.getJutsus());
        assertTrue(personagem.getJutsus().isEmpty(), "Lista de jutsus deve iniciar vazia");

        LocalDateTime now = LocalDateTime.now();
        assertNotNull(personagem.getDataCriacao());
        assertFalse(personagem.getDataCriacao().isAfter(now), "dataCriacao não deve ser no futuro");
    }

}
