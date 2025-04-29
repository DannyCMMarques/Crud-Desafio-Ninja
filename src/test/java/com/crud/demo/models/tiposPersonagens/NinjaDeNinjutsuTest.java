package com.crud.demo.models.tiposPersonagens;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.JutsuDTO;

class NinjaDeNinjutsuTest {

    @Test
    @DisplayName("Deve usar jutsu corretamente")
    void deveUsarJutsu() {
        NinjaDeNinjutsu ninja = new NinjaDeNinjutsu();
        ninja.setNome("Kakashi");

        Personagem adversario = new Personagem();
        adversario.setNome("Naruto");

        JutsuDTO jutsu = new JutsuDTO();
        jutsu.setTipo("Raikiri");
        jutsu.setDano(50);

        String resultado = ninja.usarJutsu(adversario, jutsu);

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
