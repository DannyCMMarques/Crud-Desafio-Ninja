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
                .chakra(500L)
                .build();
    }

    @Test
    @DisplayName("Deve construir Personagem com campos populados e valores padrão")
    void deveConstruirPersonagemComCamposPopuladosEPadroes() {
        assertEquals(123L, personagem.getId());
        assertEquals("Naruto Uzumaki", personagem.getNome());
        assertEquals(16L, personagem.getIdade());
        assertEquals("Konoha", personagem.getAldeia());
        assertEquals(500L, personagem.getChakra());

        assertNotNull(personagem.getJutsus());
        assertTrue(personagem.getJutsus().isEmpty(), "Lista de jutsus deve iniciar vazia");

        LocalDateTime now = LocalDateTime.now();
        assertNotNull(personagem.getDataCriacao());
        assertFalse(personagem.getDataCriacao().isAfter(now), "dataCriacao não deve ser no futuro");
    }

    @Test
    @DisplayName("Deve adicionar novo jutsu à lista de jutsus")
    void deveAdicionarNovoJutsu() {
        Jutsu rasengan = Jutsu.builder().tipo("Rasengan").build();
        personagem.adicionarJutsu(rasengan);

        assertFalse(personagem.getJutsus().isEmpty(), "Lista de jutsus não deve estar vazia após adição");
        assertEquals(1, personagem.getJutsus().size());
        assertSame(rasengan, personagem.getJutsus().get(0));
    }

    @Test
    @DisplayName("Deve aumentar o chakra corretamente")
    void deveAumentarChakraCorretamente() {
        long incremento = 250L;
        personagem.aumentarChakra(incremento);
        assertEquals(750L, personagem.getChakra(), "Chakra deve aumentar em 250");
    }

    @Test
    @DisplayName("Deve exibir informações formatadas contendo todos os dados do personagem")
    void deveExibirInformacoesFormatadasCorretamente() {
        Jutsu katon = Jutsu.builder().tipo("Katon").build();
        Jutsu raiton = Jutsu.builder().tipo("Raiton").build();
        personagem.adicionarJutsu(katon);
        personagem.adicionarJutsu(raiton);
        personagem.setChakra(300L);

        String info = personagem.exibirInformacoes();
        assertTrue(info.contains("Nome: Naruto Uzumaki"), "Deve conter nome");
        assertTrue(info.contains("Idade: 16"), "Deve conter idade");
        assertTrue(info.contains("Aldeia: Konoha"), "Deve conter aldeia");
        assertTrue(info.contains("Chakra: 300"), "Deve conter chakra atualizado");
        assertTrue(info.contains("Jutsus: [Katon, Raiton]"), "Deve listar os tipos de jutsus");
    }
}
