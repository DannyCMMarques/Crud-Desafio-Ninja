package com.crud.demo.listerners;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.DTO.websocket.DialogoDTO;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.contratos.Ninja;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.services.contratos.ChakrasService;
import com.crud.demo.services.contratos.DefesaService;
import com.crud.demo.websocket.notificacoes.DialogoNotificacoes;
import com.crud.demo.websocket.notificacoes.EstatisticasNotificacoes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatalhaEventListerner {

        private static final Logger log = LoggerFactory.getLogger(BatalhaEventListerner.class);

        private final ChakrasService chakraService;
        private final PersonagemFactoryImpl personagemFactoryImpl;
        private final PersonagemMapper personagemMapper;
        private final DefesaService defesaService;
        private final TaskScheduler scheduler;
        private final DialogoNotificacoes dialogoNotificacao;
        private final EstatisticasDoJogadorStore estatisticaStore;
        private final EstatisticasNotificacoes estatisticasNotificacoes;

        @Transactional
        @EventListener
        @Order(1)
        public void ataque(AtaqueEvent informacoesAtaque) {
                Long idBatalha = informacoesAtaque.getIdBatalha();
                EstatisticaDoJogadorEvent atacante = informacoesAtaque.getAtacante();

                JutsuResponseDTO jutsuSelecionado = informacoesAtaque.getJutsuAtaque();
                chakraService.verificarChakra(atacante, jutsuSelecionado, informacoesAtaque);
                log.info("Recebido AtaqueEvent — BatalhaID={}, PersonagemID={}, Jutsu={}",
                                idBatalha,
                                atacante.getPersonagem().getId(),
                                jutsuSelecionado.getTipo());
                Personagem personagemComTipo = personagemFactoryImpl.construirTipoPersonagem(
                                personagemMapper.toRequestDto(atacante.getPersonagem()));
                log.debug("Chakra verificado para {} após ataque {}", atacante.getPersonagem().getNome(),
                                jutsuSelecionado.getTipo());

                if (personagemComTipo instanceof Ninja) {
                        String dialogoAtaque = ((Ninja) personagemComTipo).usarJutsu(personagemComTipo,
                                        jutsuSelecionado);
                        log.info("Diálogo de ataque gerado: {}", dialogoAtaque);

                        DialogoDTO dialogoDTO = DialogoDTO.builder()
                                        .text(dialogoAtaque)
                                        .build();

                        dialogoNotificacao.enviarDialogo(idBatalha, dialogoDTO);

                        chakraService.calcularNovoChackra(idBatalha, atacante, jutsuSelecionado);

                        chakraService.calcularNovaVidaEnviarUpdate(atacante, idBatalha);
                        chakraService.calcularDerrota(informacoesAtaque);

                }

                estatisticasNotificacoes.enviarEstatisticas(idBatalha,
                                estatisticaStore.getEstatisticas(idBatalha));
                log.info("VALOR CHAKRA: " + estatisticaStore.getEstatisticas(idBatalha).get(0).getValor_chakra());
        }

        @Transactional
        @EventListener
        @Order(2)
        public void defender(AtaqueEvent informaçõesAtaque) {
                log.info("Agendando defesa para BatalhaID={} em 4 segundos", informaçõesAtaque.getIdBatalha());

                scheduler.schedule(
                                () -> defesaService.defender(informaçõesAtaque),
                                Instant.now().plusSeconds(4));
                log.debug("Iniciando lógica de defesa para BatalhaID={}", informaçõesAtaque.getIdBatalha());
                chakraService.calcularDerrota(informaçõesAtaque);

        }

}
