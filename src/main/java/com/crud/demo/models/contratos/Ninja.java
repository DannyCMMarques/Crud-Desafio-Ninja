package com.crud.demo.models.contratos;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.JutsuDTO;

public interface Ninja {

    String usarJutsu(Personagem personagem, JutsuDTO jutsuSelecionado);

    String desviar(EstatisticaDoJogadorEvent defensor);
}
