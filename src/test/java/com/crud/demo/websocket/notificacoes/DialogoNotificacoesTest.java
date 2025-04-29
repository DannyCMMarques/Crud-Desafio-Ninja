package com.crud.demo.websocket.notificacoes;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.crud.demo.models.DTO.DialogoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

class DialogoNotificacoesTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private DialogoNotificacoes dialogoNotificacoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve enviar diálogo corretamente para o tópico da batalha")
    void enviarDialogo_DeveChamarConvertAndSend() {
        Long battleId = 1L;
        DialogoDTO dialogo = DialogoDTO.builder()
                .text("A batalha começou!")
                .build();

        dialogoNotificacoes.enviarDialogo(battleId, dialogo);

        verify(messagingTemplate, times(1)).convertAndSend(
                "/topic/dialog/" + battleId,
                dialogo);
    }
}
