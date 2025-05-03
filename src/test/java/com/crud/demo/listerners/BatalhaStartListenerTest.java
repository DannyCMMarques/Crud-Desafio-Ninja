package com.crud.demo.listerners;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.events.BatalhaStartEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Batalha;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.repositories.JutsuRepository;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.services.ParticipanteBatalhaServiceImpl;
import com.crud.demo.validators.BatalhaValidators;
import com.crud.demo.websocket.notificacoes.EstatisticasNotificacoes;

@ExtendWith(MockitoExtension.class)
class BatalhaStartListenerTest {

    @Mock
    private EstatisticasDoJogadorStore store;
    @Mock
    private EstatisticasNotificacoes estatisticasNotificacoes;
    @Mock
    private JutsuRepository jutsuRepository;
    @Mock
    private PersonagemMapper personagemMapper;
    @Mock
    private ParticipanteBatalhaServiceImpl participanteService;
    @Mock
    private BatalhaValidators batalhaValidators;

    @InjectMocks
    private BatalhaStartListener listener;

    @Captor
    private ArgumentCaptor<List<EstatisticaDoJogadorEvent>> listaCaptor;

    @Test
    @DisplayName("iniciarBatalha – cria estatísticas e notifica")
    void testIniciarBatalha() {

        Long idBatalha = 1L;

        Batalha batalha = new Batalha();
        batalha.setId(idBatalha);
        when(batalhaValidators.validarExistencia(idBatalha)).thenReturn(batalha);

        PersonagemResponseDTO narutoDto = mock(PersonagemResponseDTO.class);
        when(narutoDto.getEspecialidade()).thenReturn(CategoriaEspecialidadeEnum.NINJUTSU);

        PersonagemResponseDTO leeDto = mock(PersonagemResponseDTO.class);
        when(leeDto.getEspecialidade()).thenReturn(CategoriaEspecialidadeEnum.TAIJUTSU);

        when(participanteService.getPersonagemByBatalhaId(idBatalha))
                .thenReturn(List.of(narutoDto, leeDto));

        Personagem naruto = new Personagem();
        naruto.setId(10L);
        Personagem lee = new Personagem();
        lee.setId(11L);
        when(personagemMapper.toEntity(narutoDto)).thenReturn(naruto);
        when(personagemMapper.toEntity(leeDto)).thenReturn(lee);

        when(jutsuRepository.findMapByCategoria(any()))
                .thenReturn(new HashMap<>());

        AtomicReference<List<EstatisticaDoJogadorEvent>> listaRef = new AtomicReference<>();

        doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<EstatisticaDoJogadorEvent> lista = inv.getArgument(1, List.class);
            listaRef.set(lista);
            return null;
        }).when(store).iniciarBatalha(eq(idBatalha), any());

        when(store.getEstatisticas(idBatalha))
                .thenAnswer(inv -> listaRef.get());

        listener.iniciarBatalha(new BatalhaStartEvent(idBatalha));

        verify(store).iniciarBatalha(eq(idBatalha), listaCaptor.capture());
        List<EstatisticaDoJogadorEvent> listaCriada = listaCaptor.getValue();
        assert listaCriada.size() == 2;

        verify(estatisticasNotificacoes)
                .enviarEstatisticas(eq(idBatalha), eq(listaCriada));
    }
}
