package com.crud.demo.websocket.notificacoes;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.crud.demo.events.EstatisticaDoJogadorEvent;

class EstatisticasNotificacoesTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private EstatisticasNotificacoes estatisticasNotificacoes;

    private Long battleId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        battleId = 1L;
    }

    @Test
    @DisplayName("Deve enviar estatísticas corretamente para o tópico da batalha")
    void enviarEstatisticas_DeveChamarConvertAndSend() {
        Long battleId = 1L;
        EstatisticaDoJogadorEvent estatistica = EstatisticaDoJogadorEvent.builder()
                .valor_chakra(50)
                .build();

        List<EstatisticaDoJogadorEvent> estatisticas = List.of(estatistica);

        estatisticasNotificacoes.enviarEstatisticas(battleId, estatisticas);

        verify(messagingTemplate, times(1)).convertAndSend(
                "/topic/stat/" + battleId,
                estatisticas);
    }
}
