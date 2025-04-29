package com.crud.demo.websocket.notificacoes;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.crud.demo.events.EstatisticaDoJogadorEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EstatisticasNotificacoes {

    private final SimpMessagingTemplate messagingTemplate;

    public void enviarEstatisticas(Long battleId,
            List<EstatisticaDoJogadorEvent> estatisticas) {

        log.info("[ESTATÍSTICAS] batalha={} -> {}", battleId, estatisticas.get(0).getValor_chakra());

        messagingTemplate.convertAndSend("/topic/stat/" + battleId, estatisticas);
    }
}
