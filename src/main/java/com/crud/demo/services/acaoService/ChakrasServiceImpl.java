package com.crud.demo.services.acaoService;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.events.FimDoJogoEvent;
import com.crud.demo.models.DTO.AlertaDTO;
import com.crud.demo.models.DTO.JutsuDTO;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.services.contratos.ChakrasService;
import com.crud.demo.services.contratos.JutsuService;
import com.crud.demo.websocket.notificacoes.AlertNotificacoes;
import com.crud.demo.websocket.notificacoes.EstatisticasNotificacoes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChakrasServiceImpl implements ChakrasService {

    private final ApplicationEventPublisher publisher;
    private static final Logger log = Logger.getLogger(ChakrasServiceImpl.class.getName());

    private final JutsuService jutsuService;
    private final EstatisticasDoJogadorStore estatisticaStore;
    private final EstatisticasNotificacoes estatisticasNotificacoes;
    private final AlertNotificacoes alertNotificacoes;

    @Override
    public void verificarChakra(EstatisticaDoJogadorEvent estatisticaJogador, JutsuDTO jutsuEscolhido,
            AtaqueEvent ataqueEvent) {
        calcularDerrota(ataqueEvent);
        if (estatisticaJogador.getValor_chakra() > jutsuEscolhido.getConsumo_de_chakra()) {

            jutsuService.adicionarHistorico(estatisticaJogador, jutsuEscolhido);

        } else {

            AlertaDTO alerta = new AlertaDTO("Você não possui chakra o suficiente para usar esse Jutsu");
            alertNotificacoes.enviarAlertaPrivado(estatisticaJogador.getPersonagem().getNome(), alerta);
            ataqueEvent.setJutsuAtaque(null);

        }
    }

    @Override
    public Integer calcularChakra(Integer chakraTotal, Integer chakraDescontado) {
        return chakraTotal - chakraDescontado;

    }

    @Override
    public void atualizarEstatistica(AtaqueEvent ataqueEvent) {
        Long idBatalha = ataqueEvent.getIdBatalha();
        EstatisticaDoJogadorEvent atacante = ataqueEvent.getAtacante();
        EstatisticaDoJogadorEvent defensor = ataqueEvent.getDefensor();
        JutsuDTO jutsuEscolhido = ataqueEvent.getJutsuAtaque();

        calcularNovoChackra(atacante, defensor, jutsuEscolhido);
        calcularNovaVida(atacante);
        calcularNovaVida(defensor);

        List<EstatisticaDoJogadorEvent> lista = List.of(atacante, defensor);

        estatisticaStore.updateEstatisticas(idBatalha, lista);

        estatisticasNotificacoes.enviarEstatisticas(idBatalha,
                estatisticaStore.getEstatisticas(idBatalha));
        calcularDerrota(ataqueEvent);

    }

    @Override
    public void calcularNovoChackra(EstatisticaDoJogadorEvent atacante, EstatisticaDoJogadorEvent defensor,
            JutsuDTO jutsuEscolhido) {

        Integer novoChakraAtacante = this.calcularChakra(atacante.getValor_chakra(),
                jutsuEscolhido.getConsumo_de_chakra());
        Integer novoChakraAtacado = this.calcularChakra(defensor.getValor_chakra(), jutsuEscolhido.getDano());

        atacante.setValor_chakra(novoChakraAtacante);
        defensor.setValor_chakra(novoChakraAtacado);

    }

    @Override
    public void calcularNovoChackra(Long idBatalha, EstatisticaDoJogadorEvent atacante, JutsuDTO jutsuEscolhido) {

        Integer novoChakraAtacante = this.calcularChakra(atacante.getValor_chakra(),
                jutsuEscolhido.getConsumo_de_chakra());
        log.info("Novo chakra Atacante: " + novoChakraAtacante);
        atacante.setValor_chakra(novoChakraAtacante);
        log.info("Setou Estatisticas: " + atacante);
        estatisticaStore.updateEstatistica(idBatalha, atacante);
        log.info("Atualizou Estatisticas: " + atacante);

    }

    @Override
    public void calcularNovaVida(EstatisticaDoJogadorEvent jogador) {
        Integer chakraTotal = jogador.getValor_chakra();
        Double vidas = jogador.getVidas();
        Integer chakrasPerdidos = 100 - chakraTotal;
        Integer chakrasPor10 = chakrasPerdidos / 10;
        Double perdaVida = chakrasPor10 * 0.5;
        jogador.setVidas(vidas - perdaVida);
    }

    @Override
    public void calcularNovaVidaEnviarUpdate(EstatisticaDoJogadorEvent jogador, Long idBatalha) {
        Integer chakraTotal = jogador.getValor_chakra();
        Double vidas = jogador.getVidas();
        Integer chakrasPerdidos = 100 - chakraTotal;
        Integer chakrasPor10 = chakrasPerdidos / 10;
        Double perdaVida = chakrasPor10 * 0.5;
        jogador.setVidas(vidas - perdaVida);
        estatisticaStore.updateEstatistica(idBatalha, jogador);
    }

    @Override
    @Transactional
    public void calcularDerrota(AtaqueEvent ataqueEvent) {
        Long idBatalha = ataqueEvent.getIdBatalha();
        log.info(String.format("[CalcularDerrota] Iniciando cálculo de derrota para batalha ID: %d", idBatalha));

        EstatisticaDoJogadorEvent atacante = ataqueEvent.getAtacante();
        EstatisticaDoJogadorEvent defensor = ataqueEvent.getDefensor();

        log.info(String.format("[CalcularDerrota] Dados do Atacante - ID: %d",
                atacante.getPersonagem().getId()));
        log.info(String.format("[CalcularDerrota] Dados do Defensor - ID: %d",
                defensor.getPersonagem().getId()));
        log.info(String.format("[CalcularDerrota] Vida do atacante: %.2f, Vida do defensor: %.2f",
                atacante.getVidas(),
                defensor.getVidas()
        ));
        if (defensor.getValor_chakra() <= 0 || defensor.getVidas() <= 0) {
            log.warning(String.format("[CalcularDerrota] Defensor derrotado - ID: %d", defensor.getPersonagem().getId()));

            FimDoJogoEvent fimDoJogo = new FimDoJogoEvent(atacante.getPersonagem().getId(), idBatalha);
            log.info(String.format("[CalcularDerrota] Publicando evento de vitória para Atacante ID: %d", atacante.getPersonagem().getId()));

            publisher.publishEvent(fimDoJogo);
            return;
        }
        if (atacante.getValor_chakra() <= 0 || atacante.getVidas() <= 0) {
            log.warning(String.format("[CalcularDerrota] Atacante derrotado - ID: %d", atacante.getPersonagem().getId()));

            FimDoJogoEvent fimDoJogo = new FimDoJogoEvent(defensor.getPersonagem().getId(), idBatalha);
            log.info(String.format("[CalcularDerrota] Publicando evento de vitória para Defensor ID: %d", defensor.getPersonagem().getId()));

            publisher.publishEvent(fimDoJogo);
            return;
        }
        log.info(String.format("[CalcularDerrota] Fim do cálculo para batalha ID: %d", idBatalha));

    }

}
