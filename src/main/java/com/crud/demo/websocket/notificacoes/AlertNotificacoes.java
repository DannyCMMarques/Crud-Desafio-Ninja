package com.crud.demo.websocket.notificacoes;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.crud.demo.models.DTO.AlertaDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertNotificacoes {

    private final SimpMessagingTemplate messagingTemplate;

    public void enviarGlobalAlerta(Long battleId, AlertaDTO mensagem) {

        log.info("[ALERTA] batalha={} -> {}", battleId, mensagem);

        messagingTemplate.convertAndSend(
                "/topic/alert/" + battleId,
                mensagem);
    }

    public void enviarAlertaPrivado(String username, AlertaDTO mensagem) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/alert",
                mensagem);
    }
}
