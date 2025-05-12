package com.crud.demo.services;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.DTO.websocket.DialogoDTO;
import com.crud.demo.models.contratos.Ninja;
import com.crud.demo.models.tiposPersonagens.NinjaDeGenjutsu;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.services.contratos.AtacarService;
import com.crud.demo.services.contratos.ChakrasService;
import com.crud.demo.websocket.notificacoes.DialogoNotificacoes;

@ExtendWith(MockitoExtension.class)
class DefesaServiceImplTest {

    @Mock private PersonagemFactoryImpl personagemFactory;
    @Mock private AtacarService atacarService;
    @Mock private PersonagemMapper personagemMapper;
    @Mock private DialogoNotificacoes dialogoNotificacoes;
    @Mock private ChakrasService chakrasService;

    @InjectMocks
    private DefesaServiceImpl service;

    @Captor
    private ArgumentCaptor<DialogoDTO> dialogoCaptor;

    private AtaqueEvent evento;
    private EstatisticaDoJogadorEvent defensorStat;
    private PersonagemRequestDTO dummyDto;

    @BeforeEach
    void setUp() {
        defensorStat = new EstatisticaDoJogadorEvent();
        Personagem defensor = new Personagem();
        defensor.setId(2L);
        defensor.setNome("Defensor");
        defensorStat.setPersonagem(defensor);

        evento = mock(AtaqueEvent.class);
        when(evento.getIdBatalha()).thenReturn(42L);
        when(evento.getDefensor()).thenReturn(defensorStat);

        dummyDto = mock(PersonagemRequestDTO.class);
        when(personagemMapper.toRequestDto(defensor)).thenReturn(dummyDto);
    }

    @Test
    @DisplayName("quando defensor tem chakra alto, deve desviar e não atacar")
    void testDefenderSuccessfulDefense() {
        defensorStat.setValor_chakra(200);

        Personagem ninjaPersona = spy(new NinjaDeGenjutsu());
        when(personagemFactory.construirTipoPersonagem(dummyDto)).thenReturn(ninjaPersona);
        doReturn("desviou com sucesso")
            .when((Ninja) ninjaPersona)
            .desviar(defensorStat);

        service.defender(evento);

        verify(dialogoNotificacoes).enviarDialogo(eq(42L), dialogoCaptor.capture());
        assert dialogoCaptor.getValue().getText().equals("desviou com sucesso");

        verify(atacarService, never()).atacar(any());
        verify(chakrasService, never()).calcularDerrota(any());
    }

    @Test
    @DisplayName("quando defensor tem chakra baixo, deve falhar defesa e atacar")
    void testDefenderFailureAndAttack() {
        defensorStat.setValor_chakra(1);

        Personagem ninjaPersona = spy(new NinjaDeGenjutsu());
        ninjaPersona.setNome("Defensor");
        when(personagemFactory.construirTipoPersonagem(dummyDto)).thenReturn(ninjaPersona);

        service.defender(evento);

        verify(dialogoNotificacoes).enviarDialogo(eq(42L), dialogoCaptor.capture());
        assert dialogoCaptor.getValue()
                     .getText()
                     .equals("Defensor não conseguiu se defender");

        verify(atacarService).atacar(evento);
        verify(chakrasService).calcularDerrota(evento);
    }
}
