package com.crud.demo.services.contratos;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.DTO.JutsuDTO;

public interface ChakrasService {

    void verificarChakra(EstatisticaDoJogadorEvent estatisticaJogador, JutsuDTO jutsuEscolhido,
            AtaqueEvent ataqueEvent);

    Integer calcularChakra(Integer chakraTotal, Integer chakraDescontado);

    void atualizarEstatistica(AtaqueEvent ataqueEvent);

    void calcularNovoChackra(EstatisticaDoJogadorEvent atacante, EstatisticaDoJogadorEvent defensor,
            JutsuDTO jutsuEscolhido);

    void calcularNovoChackra(Long idBatalha, EstatisticaDoJogadorEvent atacante, JutsuDTO jutsuEscolhido);

    void calcularNovaVida(EstatisticaDoJogadorEvent jogador);

    void calcularNovaVidaEnviarUpdate(EstatisticaDoJogadorEvent jogador, Long idBatalha);

    void calcularDerrota(AtaqueEvent ataqueEvent);

}
