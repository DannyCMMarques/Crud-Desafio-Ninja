package com.crud.demo.models.tiposPersonagens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;

class NinjaDeTaijutsuTest {

    private NinjaDeTaijutsu ninja;

    @BeforeEach
    void setUp() {
        ninja = new NinjaDeTaijutsu();
        ninja.setNome("Rock Lee");
    }

    @Test
    @DisplayName("Deve usar jutsu corretamente como ninja de Taijutsu")
    void deveUsarJutsu() {
        JutsuResponseDTO jutsu = JutsuResponseDTO.builder()
            .tipo("Lótus Primária")
            .dano(80)
            .build();
        Personagem alvo = new Personagem();
        alvo.setNome("Gaara");

        String resultado = ninja.usarJutsu(alvo, jutsu);

        String esperado = "Houve um ataque: Rock Lee,um ninja de Taijutsu, usa 'Lótus Primária' e causa a perda de 80 chakras no adversário";
        assertEquals(esperado, resultado);
    }

    @Test
    @DisplayName("Deve desviar corretamente como ninja de Taijutsu")
    void deveDesviar() {
        EstatisticaDoJogadorEvent defensor = new EstatisticaDoJogadorEvent();

        String resultado = ninja.desviar(defensor);

        String esperado = "Defesa: Rock Lee,um ninja de Taijutsu, desviou do ataque ";
        assertEquals(esperado, resultado);
    }
}
