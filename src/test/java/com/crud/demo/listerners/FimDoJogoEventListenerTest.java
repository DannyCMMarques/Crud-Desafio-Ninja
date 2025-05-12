package com.crud.demo.listerners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.events.FimDoJogoEvent;
import com.crud.demo.models.Batalha;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.models.DTO.websocket.DialogoDTO;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.mappers.BatalhaMapper;
import com.crud.demo.services.BatalhaServiceImpl;
import com.crud.demo.websocket.notificacoes.DialogoNotificacoes;

@ExtendWith(MockitoExtension.class)
class FimDoJogoEventListenerTest {

    @Mock
    private BatalhaServiceImpl batalhaService;
    @Mock
    private DialogoNotificacoes dialogoNotificacoes;
    @Mock
    private BatalhaMapper batalhaMapper;

    @InjectMocks
    private FimDoJogoEventListener listener;

    @Captor
    private ArgumentCaptor<DialogoDTO> dialogoCaptor;

    @Test
    @DisplayName("finalizarJogo – marca vencedor e envia diálogo com nome correto")
    void testFinalizarJogo() {
        Long idBatalha = 1L;
        Long idGanhador = 100L;

        Personagem sasuke = spy(new Personagem());
        sasuke.setId(idGanhador);
        sasuke.setNome("Sasuke");
        doReturn("Sasuke").when(sasuke).toString();

        ParticipanteBatalha vencedor = new ParticipanteBatalha();
        vencedor.setId(idGanhador);
        vencedor.setPersonagem(sasuke);

        Personagem naruto = new Personagem();
        naruto.setId(101L);
        naruto.setNome("Naruto");
        ParticipanteBatalha perdedor = new ParticipanteBatalha();
        perdedor.setId(101L);
        perdedor.setPersonagem(naruto);

        List<ParticipanteBatalha> participantes = Arrays.asList(vencedor, perdedor);

        BatalhaResponseDTO batalhaDto = mock(BatalhaResponseDTO.class);
        when(batalhaDto.getParticipantesBatalha()).thenReturn(participantes);
        when(batalhaService.getBatalhaById(idBatalha)).thenReturn(batalhaDto);

        Batalha batalhaEntity = new Batalha();
        when(batalhaMapper.toEntity(batalhaDto)).thenReturn(batalhaEntity);

        FimDoJogoEvent evento = new FimDoJogoEvent(idGanhador, idBatalha);
        listener.finalizarJogo(evento);

        assertTrue(vencedor.getVencedor(), "Vencedor deveria estar marcado");
        assertFalse(perdedor.getVencedor(), "Perdedor não deve ser marcado");

        assertNotNull(batalhaEntity.getFinalizadoEm());

        verify(dialogoNotificacoes).enviarDialogo(eq(idBatalha), dialogoCaptor.capture());
        String texto = dialogoCaptor.getValue().getText();
        assertTrue(texto.contains("Sasuke"), "Texto deve conter 'Sasuke'");
    }
}
