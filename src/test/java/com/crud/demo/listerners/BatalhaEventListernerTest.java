package com.crud.demo.listerners;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.DialogoDTO;
import com.crud.demo.models.DTO.JutsuDTO;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.models.tiposPersonagens.NinjaDeGenjutsu;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.services.contratos.ChakrasService;
import com.crud.demo.services.contratos.DefesaService;
import com.crud.demo.websocket.notificacoes.DialogoNotificacoes;
import com.crud.demo.websocket.notificacoes.EstatisticasNotificacoes;

@ExtendWith(MockitoExtension.class) 
class BatalhaEventListernerTest {

    @Mock
    private ChakrasService chakraService;

    @Mock
    private PersonagemFactoryImpl personagemFactoryImpl;

    @Mock
    private PersonagemMapper personagemMapper;

    @Mock
    private DefesaService defesaService;

    @Mock
    private TaskScheduler scheduler;

    @Mock
    private DialogoNotificacoes dialogoNotificacao;

    @Mock
    private EstatisticasDoJogadorStore estatisticaStore;

    @Mock
    private EstatisticasNotificacoes estatisticasNotificacoes;

    @InjectMocks 
    private BatalhaEventListerner batalhaEventListerner;

    @Test
    void deveExecutarAtaqueComSucesso() {
        Personagem personagem = new Personagem();
        PersonagemDTO personagemDTO = new PersonagemDTO();
        NinjaDeGenjutsu ninjaDeGentujsu = new NinjaDeGenjutsu();
        JutsuDTO jutsu = new JutsuDTO();
        jutsu.setTipo("Chidori");

        EstatisticaDoJogadorEvent atacante = new EstatisticaDoJogadorEvent();
        atacante.setPersonagem(personagem);

        AtaqueEvent ataqueEvent = new AtaqueEvent(1L, atacante, new EstatisticaDoJogadorEvent(), jutsu);

        when(personagemMapper.toDto(personagem)).thenReturn(personagemDTO);
        when(personagemFactoryImpl.construirTipoPersonagem(personagemDTO)).thenReturn(ninjaDeGentujsu);

        when(estatisticaStore.getEstatisticas(anyLong())).thenReturn(List.of(atacante));

        batalhaEventListerner.ataque(ataqueEvent);

        verify(chakraService).verificarChakra(atacante, jutsu, ataqueEvent);
        verify(dialogoNotificacao).enviarDialogo(eq(1L), any(DialogoDTO.class));
        verify(chakraService).calcularNovoChackra(1L, atacante, jutsu);
        verify(chakraService).calcularNovaVidaEnviarUpdate(atacante, 1L);
        verify(chakraService).calcularDerrota(ataqueEvent);
        verify(estatisticasNotificacoes).enviarEstatisticas(eq(1L), any());
    }

    @Test
    void deveExecutarDefesaComSucesso() {
        AtaqueEvent ataqueEvent = mock(AtaqueEvent.class);
        when(ataqueEvent.getIdBatalha()).thenReturn(1L);

        ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);

        batalhaEventListerner.defender(ataqueEvent);

        verify(scheduler).schedule(any(Runnable.class), any(Instant.class));
        verify(chakraService).calcularDerrota(ataqueEvent);
    }
}
