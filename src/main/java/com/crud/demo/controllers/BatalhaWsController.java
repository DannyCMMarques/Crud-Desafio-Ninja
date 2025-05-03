package com.crud.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.BatalhaStartEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.DTO.websocket.AtaqueRequestDTO;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.services.contratos.BatalhaService;
import com.crud.demo.services.contratos.JutsuService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BatalhaWsController {

        private static final Logger log = LoggerFactory.getLogger(BatalhaWsController.class);

        private final ApplicationEventPublisher publisher;
        private final JutsuService jutsuService;
        private final EstatisticasDoJogadorStore estatisticasStore;
        private final BatalhaService batalhaService;

        @MessageMapping("/batalha/{idBatalha}/start")
        public void comecarBatalha(@DestinationVariable Long idBatalha) {

                batalhaService.verificarBatalhaEncerrada(idBatalha);

                log.info("🡆 comecarBatalha chamado com idBatalha={}", idBatalha);

                publisher.publishEvent(new BatalhaStartEvent(idBatalha));

        }

        @MessageMapping("/batalha/{idBatalha}/ataque/{personagemId}")
        public void atacar(
                        @DestinationVariable Long idBatalha,
                        @Payload AtaqueRequestDTO ataqueReq) {
                batalhaService.verificarBatalhaEncerrada(idBatalha);

                log.info("🡆 atacar chamado idBatalha={} payload={}", idBatalha, ataqueReq);

                EstatisticaDoJogadorEvent estatisticasAtacante = estatisticasStore.getEstatistica(idBatalha,
                                ataqueReq.getIdAtacante());
                EstatisticaDoJogadorEvent estatisticasDefensor = estatisticasStore.getEstatistica(idBatalha,
                                ataqueReq.getIdDefensor());
                log.info("Jutsu", ataqueReq.getJutsuEscolhido());

                JutsuResponseDTO jutsu = jutsuService.getJutsuByTipo(ataqueReq.getJutsuEscolhido());

                AtaqueEvent ataqueEvent = new AtaqueEvent(idBatalha, estatisticasAtacante, estatisticasDefensor,  jutsu);

                publisher.publishEvent(ataqueEvent);

        }

}
