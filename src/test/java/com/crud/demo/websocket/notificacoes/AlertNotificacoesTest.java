package com.crud.demo.websocket.notificacoes;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.crud.demo.models.DTO.AlertaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

class AlertNotificacoesTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private AlertNotificacoes alertNotificacoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve enviar alerta global corretamente")
    void enviarGlobalAlerta_DeveChamarConvertAndSend() {
        Long battleId = 1L;
        AlertaDTO alerta = AlertaDTO.builder()
                .mensagem("Batalha iniciada!")
                .build();

        alertNotificacoes.enviarGlobalAlerta(battleId, alerta);

        verify(messagingTemplate, times(1)).convertAndSend(
                "/topic/alert/" + battleId,
                alerta);
    }

    @Test
    @DisplayName("Deve enviar alerta privado corretamente")
    void enviarAlertaPrivado_DeveChamarConvertAndSendToUser() {
        String username = "player1";
        AlertaDTO alerta = AlertaDTO.builder()
                .mensagem("Você foi atacado!")
                .build();

        alertNotificacoes.enviarAlertaPrivado(username, alerta);

        verify(messagingTemplate, times(1)).convertAndSendToUser(
                username,
                "/queue/alert",
                alerta);
    }
}
