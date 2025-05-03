package com.crud.demo.services; // ⬅️ um único package‑statement

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.events.FimDoJogoEvent;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.DTO.websocket.AlertaDTO;
import com.crud.demo.models.Personagem;
import com.crud.demo.services.contratos.JutsuService;
import com.crud.demo.websocket.notificacoes.AlertNotificacoes;
import com.crud.demo.websocket.notificacoes.EstatisticasNotificacoes;

@ExtendWith(MockitoExtension.class)
class ChakrasServiceImplTest {

    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private JutsuService jutsuService;
    @Mock
    private EstatisticasDoJogadorStore estatisticaStore;
    @Mock
    private EstatisticasNotificacoes estatisticasNotificacoes;
    @Mock
    private AlertNotificacoes alertNotificacoes;

    @InjectMocks
    private ChakrasServiceImpl chakrasService;

    private EstatisticaDoJogadorEvent atacante;
    private EstatisticaDoJogadorEvent defensor;
    private JutsuResponseDTO jutsuDto;
    private AtaqueEvent ataqueEvent;

    @BeforeEach
    void setUp() {

        Personagem personagemAtacante = Personagem.builder()
                .id(1L).nome("Atacante").build();

        Personagem personagemDefensor = Personagem.builder()
                .id(2L).nome("Defensor").build();

        atacante = EstatisticaDoJogadorEvent.builder()
                .personagem(personagemAtacante)
                .valor_chakra(100)
                .vidas(10.0)
                .build();

        defensor = EstatisticaDoJogadorEvent.builder()
                .personagem(personagemDefensor)
                .valor_chakra(100)
                .vidas(10.0)
                .build();

        jutsuDto = JutsuResponseDTO.builder()
                .id(1L)
                .consumo_de_chakra(20)
                .dano(30)
                .build();

        ataqueEvent = AtaqueEvent.builder()
                .idBatalha(1L)
                .atacante(atacante)
                .defensor(defensor)
                .jutsuAtaque(jutsuDto)
                .build();
    }


    @Test
    @DisplayName("verificarChakra – deve adicionar histórico quando há chakra suficiente")
    void deveAdicionarHistoricoQuandoTemChakraSuficiente() {

        chakrasService.verificarChakra(atacante, jutsuDto, ataqueEvent);

        verify(jutsuService, times(1)).adicionarHistorico(atacante, jutsuDto);
        assertThat(ataqueEvent.getJutsuAtaque()).isEqualTo(jutsuDto);
    }

    @Test
    @DisplayName("verificarChakra – deve enviar alerta quando falta chakra")
    void deveEnviarAlertaQuandoNaoTemChakraSuficiente() {

        atacante.setValor_chakra(10);

        chakrasService.verificarChakra(atacante, jutsuDto, ataqueEvent);

        verify(alertNotificacoes, times(1))
                .enviarAlertaPrivado(eq("Atacante"), any(AlertaDTO.class));
        assertThat(ataqueEvent.getJutsuAtaque()).isNull();
    }

    @Test
    @DisplayName("calcularChakra – deve retornar chakraTotal - chakraDescontado")
    void deveCalcularChakraCorretamente() {
        assertThat(chakrasService.calcularChakra(100, 30)).isEqualTo(70);
    }

    @Test
    @DisplayName("atualizarEstatistica – deve atualizar store, notificar e (opcionalmente) publicar fim de jogo")
    void deveAtualizarEstatistica() {

        chakrasService.atualizarEstatistica(ataqueEvent);

        verify(estatisticaStore, times(1))
                .updateEstatisticas(eq(1L), anyList());
        verify(estatisticasNotificacoes, times(1))
                .enviarEstatisticas(eq(1L), anyList());

        verify(publisher, atLeast(0)).publishEvent(any(FimDoJogoEvent.class));
    }

    @Test
    @DisplayName("calcularNovoChackra – deve subtrair consumo e dano para atacante e defensor")
    void deveCalcularNovoChakraParaAtacanteEDefensor() {

        chakrasService.calcularNovoChackra(atacante, defensor, jutsuDto);

        assertThat(atacante.getValor_chakra()).isEqualTo(80); 
        assertThat(defensor.getValor_chakra()).isEqualTo(70); 
    }

    @Test
    @DisplayName("calcularNovoChackra (com store) – deve atualizar atacante na store")
    void deveCalcularNovoChakraEAtualizarEstatistica() {

        chakrasService.calcularNovoChackra(1L, atacante, jutsuDto);

        assertThat(atacante.getValor_chakra()).isEqualTo(80);
        verify(estatisticaStore, times(1))
                .updateEstatistica(1L, atacante);
    }

    @Test
    @DisplayName("calcularNovaVida – deve reduzir vida de acordo com chakras perdidos")
    void deveCalcularNovaVida() {

        atacante.setValor_chakra(40);

        chakrasService.calcularNovaVida(atacante);

        assertThat(atacante.getVidas()).isEqualTo(7.0);
    }

    @Test
    @DisplayName("calcularNovaVidaEnviarUpdate – deve salvar na store também")
    void deveCalcularNovaVidaEAtualizarEstatistica() {

        atacante.setValor_chakra(30);

        chakrasService.calcularNovaVidaEnviarUpdate(atacante, 1L);

        verify(estatisticaStore, times(1))
                .updateEstatistica(1L, atacante);
        assertThat(atacante.getVidas()).isLessThan(10.0);
    }

    @Test
    @DisplayName("calcularDerrota – deve publicar evento quando defensor é derrotado")
    void devePublicarEventoFimDoJogoQuandoDefensorPerdeVidaOuChakra() {

        defensor.setValor_chakra(0);

        chakrasService.calcularDerrota(ataqueEvent);

        verify(publisher, times(1))
                .publishEvent(any(FimDoJogoEvent.class));
    }

    @Test
    @DisplayName("calcularDerrota – deve publicar evento quando atacante é derrotado")
    void devePublicarEventoFimDoJogoQuandoAtacantePerdeVidaOuChakra() {

        atacante.setValor_chakra(0);

        chakrasService.calcularDerrota(ataqueEvent);

        verify(publisher, times(1))
                .publishEvent(any(FimDoJogoEvent.class));
    }
}
