package com.crud.demo.listerners;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.DTO.websocket.DialogoDTO;
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

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;
    @Captor
    private ArgumentCaptor<Instant> instantCaptor;
    @Captor
    private ArgumentCaptor<DialogoDTO> dialogoCaptor;
    @Captor
    private ArgumentCaptor<List<EstatisticaDoJogadorEvent>> listaEstatisticasCaptor;

    @Test
    @DisplayName("ataque – deve executar fluxo completo")
    void deveExecutarAtaqueComSucesso() {

        Personagem personagem = new Personagem();
        personagem.setId(999L);
        personagem.setNome("Sasuke");

        PersonagemRequestDTO requestDTO = new PersonagemRequestDTO();
        NinjaDeGenjutsu ninja = new NinjaDeGenjutsu();

        JutsuResponseDTO jutsu = JutsuResponseDTO.builder()
                .tipo("Chidori")
                .build();

        EstatisticaDoJogadorEvent atacante = new EstatisticaDoJogadorEvent();
        atacante.setPersonagem(personagem);

        AtaqueEvent ataqueEvent = new AtaqueEvent(
                1L, atacante, new EstatisticaDoJogadorEvent(), jutsu);

        when(personagemMapper.toRequestDto(personagem)).thenReturn(requestDTO);
        when(personagemFactoryImpl.construirTipoPersonagem(requestDTO)).thenReturn(ninja);
        when(estatisticaStore.getEstatisticas(1L)).thenReturn(List.of(atacante));

        batalhaEventListerner.ataque(ataqueEvent);

        verify(chakraService).verificarChakra(atacante, jutsu, ataqueEvent);
        verify(dialogoNotificacao).enviarDialogo(eq(1L), dialogoCaptor.capture());
        verify(chakraService).calcularNovoChackra(1L, atacante, jutsu);
        verify(chakraService).calcularNovaVidaEnviarUpdate(atacante, 1L);
        verify(chakraService).calcularDerrota(ataqueEvent);
        verify(estatisticasNotificacoes)
                .enviarEstatisticas(eq(1L), listaEstatisticasCaptor.capture());

        DialogoDTO dialogoEnviado = dialogoCaptor.getValue();
        assert dialogoEnviado.getText() != null && !dialogoEnviado.getText().isBlank();

        List<EstatisticaDoJogadorEvent> lista = listaEstatisticasCaptor.getValue();
        assert lista.size() == 1 && lista.get(0) == atacante;
    }

    @Test
    @DisplayName("defender – agenda Runnable e recalcula derrota")
    void deveExecutarDefesaComSucesso() {

        AtaqueEvent ataqueEvent = mock(AtaqueEvent.class);
        when(ataqueEvent.getIdBatalha()).thenReturn(1L);

        when(scheduler.schedule(runnableCaptor.capture(), instantCaptor.capture()))
                .thenReturn(null);

        batalhaEventListerner.defender(ataqueEvent);

        verify(scheduler).schedule(runnableCaptor.getValue(), instantCaptor.getValue());
        verify(chakraService).calcularDerrota(ataqueEvent);

        runnableCaptor.getValue().run();
        verify(defesaService).defender(ataqueEvent);
    }
}
