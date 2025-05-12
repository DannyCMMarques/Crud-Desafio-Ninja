package com.crud.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.BatalhaStartEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.DTO.websocket.AtaqueRequestDTO;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.mappers.JutsuMapper;
import com.crud.demo.services.BatalhaServiceImpl;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.services.contratos.JutsuService;

@ExtendWith(MockitoExtension.class)
class BatalhaWsControllerTest {

        @Mock
        private ApplicationEventPublisher publisher;

        @Mock
        private JutsuService jutsuService;

        @Mock
        private EstatisticasDoJogadorStore estatisticasStore;

        @Mock
        private JutsuMapper jutsuMapper;

        @Mock
        private BatalhaServiceImpl batalhasService;

        @InjectMocks
        private BatalhaWsController controller;

        private AtaqueRequestDTO ataqueRequest;
        private EstatisticaDoJogadorEvent atacante;
        private EstatisticaDoJogadorEvent defensor;
        private Jutsu jutsu;
        private JutsuResponseDTO JutsuResponseDTO;

        @BeforeEach
        void setUp() {
                ataqueRequest = new AtaqueRequestDTO();
                ataqueRequest.setIdAtacante(1L);
                ataqueRequest.setIdDefensor(2L);
                ataqueRequest.setJutsuEscolhido("Rasengan");

                atacante = EstatisticaDoJogadorEvent.builder()
                                .personagem(Personagem.builder().id(1L).nome("Naruto").build())
                                .valor_chakra(100)
                                .vidas(10.0)
                                .build();

                defensor = EstatisticaDoJogadorEvent.builder()
                                .personagem(Personagem.builder().id(2L).nome("Sasuke").build())
                                .valor_chakra(100)
                                .vidas(10.0)
                                .build();

                jutsu = Jutsu.builder()
                                .id(1L)
                                .tipo("Rasengan")
                                .build();

                JutsuResponseDTO = JutsuResponseDTO.builder()
                                .id(1L)
                                .tipo("Rasengan")
                                .build();
        }

        @Test
        @DisplayName("Deve iniciar batalha e publicar evento BatalhaStartEvent")
        void deveIniciarBatalha() {
                Long idBatalha = 1L;

                controller.comecarBatalha(idBatalha);

                verify(batalhasService, times(1)).verificarBatalhaEncerrada(idBatalha);
                verify(publisher, times(1)).publishEvent(any(BatalhaStartEvent.class));
        }

        @Test
        @DisplayName("Deve processar ataque corretamente e publicar evento AtaqueEvent")
        void deveAtacar() {
                Long idBatalha = 1L;

                when(estatisticasStore.getEstatistica(idBatalha, ataqueRequest.getIdAtacante()))
                                .thenReturn(atacante);

                when(estatisticasStore.getEstatistica(idBatalha, ataqueRequest.getIdDefensor()))
                                .thenReturn(defensor);

                when(jutsuService.getJutsuByTipo("Rasengan"))
                                .thenReturn(JutsuResponseDTO);

                controller.atacar(idBatalha, ataqueRequest);

                verify(batalhasService, times(1)).verificarBatalhaEncerrada(idBatalha);
                verify(estatisticasStore, times(1)).getEstatistica(idBatalha, ataqueRequest.getIdAtacante());
                verify(estatisticasStore, times(1)).getEstatistica(idBatalha, ataqueRequest.getIdDefensor());
                verify(jutsuService, times(1)).getJutsuByTipo("Rasengan");
                verify(publisher, times(1)).publishEvent(any(AtaqueEvent.class));
        }
}
