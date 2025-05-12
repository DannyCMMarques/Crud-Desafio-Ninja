package com.crud.demo.models.tiposPersonagens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;

class NinjaDeNinjutsuTest {
    @InjectMocks
      private NinjaDeNinjutsu ninjaDeNinjutsu;

      @BeforeEach
        void setUp() {
            ninjaDeNinjutsu = new NinjaDeNinjutsu();
            ninjaDeNinjutsu.setNome("Kakashi");
        }

    @Test
    @DisplayName("Deve usar jutsu corretamente")
    void deveUsarJutsu() {
        Personagem adversario = new Personagem();
        adversario.setNome("Naruto");

        JutsuResponseDTO jutsu = JutsuResponseDTO.builder()
            .tipo("Raikiri")
            .dano(50)
            .build();

        String resultado = ninjaDeNinjutsu.usarJutsu(adversario, jutsu);

        String esperado = "Houve um ataque: Kakashi,um ninja de Ninjutsu, usa 'Raikiri' e causa a perda de 50 chakras no adversário";
        assertEquals(esperado, resultado);
    }

    @Test
    @DisplayName("Deve desviar corretamente")
    void deveDesviar() {
        NinjaDeNinjutsu ninja = new NinjaDeNinjutsu();
        ninja.setNome("Kakashi");

        EstatisticaDoJogadorEvent defensor = new EstatisticaDoJogadorEvent();

        String resultado = ninja.desviar(defensor);

        String esperado = "Defesa: Kakashi,um ninja de Ninjutsu, desviou do ataque ";
        assertEquals(esperado, resultado);
    }
}
