package com.crud.demo.websocket.notificacoes;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.crud.demo.models.DTO.websocket.DialogoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DialogoNotificacoes {

    private final SimpMessagingTemplate messagingTemplate;

    public void enviarDialogo(Long battleId, DialogoDTO mensagem) {

        log.info("[DIALOGO] batalha={} -> {}", battleId, mensagem);

        messagingTemplate.convertAndSend("/topic/dialog/" + battleId, mensagem);
    }
}
