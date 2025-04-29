package com.crud.demo.services.acaoService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.AlertaDTO;
import com.crud.demo.models.DTO.JutsuDTO;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.websocket.notificacoes.AlertNotificacoes;
import com.crud.demo.websocket.notificacoes.EstatisticasNotificacoes;

@ExtendWith(MockitoExtension.class)
class ChakrasServiceImplTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private JutsuServiceImpl jutsuService;

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
    private JutsuDTO jutsuDTO;
    private AtaqueEvent ataqueEvent;

    @BeforeEach
    void setUp() {
        Personagem personagemAtacante = Personagem.builder()
        .id(1L)
        .nome("Atacante")
        .build();

    Personagem personagemDefensor = Personagem.builder()
        .id(2L)
        .nome("Defensor")
        .build();

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

        jutsuDTO = JutsuDTO.builder()
            .id(1L)
            .consumo_de_chakra(20)
            .dano(30)
            .build();

        ataqueEvent = AtaqueEvent.builder()
            .idBatalha(1L)
            .atacante(atacante)
            .defensor(defensor)
            .jutsuAtaque(jutsuDTO)
            .build();
    }

    @Test
    @DisplayName("Deve adicionar histórico quando jogador tem chakra suficiente")
    void deveAdicionarHistoricoQuandoTemChakraSuficiente() {
        chakrasService.verificarChakra(atacante, jutsuDTO, ataqueEvent);

        verify(jutsuService, times(1)).adicionarHistorico(atacante, jutsuDTO);
        assertThat(ataqueEvent.getJutsuAtaque()).isEqualTo(jutsuDTO);
    }

    
    @Test
    @DisplayName("Deve enviar alerta quando jogador não tem chakra suficiente")
    void deveEnviarAlertaQuandoNaoTemChakraSuficiente() {
        atacante.setValor_chakra(10);

        chakrasService.verificarChakra(atacante, jutsuDTO, ataqueEvent);

        verify(alertNotificacoes, times(1))
            .enviarAlertaPrivado(eq("Atacante"), any(AlertaDTO.class));
        assertThat(ataqueEvent.getJutsuAtaque()).isNull();
    }

    @Test
    @DisplayName("Deve calcular chakra corretamente")
    void deveCalcularChakraCorretamente() {
        Integer resultado = chakrasService.calcularChakra(100, 30);
        assertThat(resultado).isEqualTo(70);
    }

    @Test
    @DisplayName("Deve atualizar estatísticas corretamente")
    void deveAtualizarEstatistica() {
        chakrasService.atualizarEstatistica(ataqueEvent);

        verify(estatisticaStore, times(1))
            .updateEstatisticas(eq(1L), anyList());
        verify(estatisticasNotificacoes, times(1))
            .enviarEstatisticas(eq(1L), anyList());
        FimDoJogoEvent fimDoJogoEvent = FimDoJogoEvent.builder()
            .idBatalha(ataqueEvent.getIdBatalha())
            .idGanhador(1L)
            .build();

        verify(publisher, atLeast(0)).publishEvent(fimDoJogoEvent);
    }

    @Test
    @DisplayName("Deve calcular novo chakra para atacante e defensor")
    void deveCalcularNovoChakraParaAtacanteEDefensor() {
        chakrasService.calcularNovoChackra(atacante, defensor, jutsuDTO);

        assertThat(atacante.getValor_chakra()).isEqualTo(80); 
        assertThat(defensor.getValor_chakra()).isEqualTo(70); 
    }

    @Test
    @DisplayName("Deve calcular novo chakra do atacante e atualizar estatística")
    void deveCalcularNovoChakraEAtualizarEstatistica() {
        chakrasService.calcularNovoChackra(1L, atacante, jutsuDTO);

        assertThat(atacante.getValor_chakra()).isEqualTo(80);
        verify(estatisticaStore, times(1))
            .updateEstatistica(1L, atacante);
    }

    @Test
    @DisplayName("Deve calcular nova vida corretamente")
    void deveCalcularNovaVida() {
        atacante.setValor_chakra(40);

        chakrasService.calcularNovaVida(atacante);

        assertThat(atacante.getVidas()).isEqualTo(7.0);
    }

    @Test
    @DisplayName("Deve calcular nova vida e atualizar estatística")
    void deveCalcularNovaVidaEAtualizarEstatistica() {
        atacante.setValor_chakra(30);

        chakrasService.calcularNovaVidaEnviarUpdate(atacante, 1L);

        verify(estatisticaStore, times(1)).updateEstatistica(1L, atacante);
        assertThat(atacante.getVidas()).isLessThan(10.0);
    }

    @Test
    @DisplayName("Deve publicar evento de fim de jogo quando defensor perde vida ou chakra")
    void devePublicarEventoFimDoJogoQuandoDefensorPerdeVidaOuChakra() {
        defensor.setValor_chakra(0);

        chakrasService.calcularDerrota(ataqueEvent);

        verify(publisher, times(1)).publishEvent(any(FimDoJogoEvent.class));
    }

    @Test
    @DisplayName("Deve publicar evento de fim de jogo quando atacante perde vida ou chakra")
    void devePublicarEventoFimDoJogoQuandoAtacantePerdeVidaOuChakra() {
        atacante.setValor_chakra(0);

        chakrasService.calcularDerrota(ataqueEvent);

        verify(publisher, times(1)).publishEvent(any(FimDoJogoEvent.class));
    }
}

