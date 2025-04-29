package com.crud.demo.listerners;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.crud.demo.events.FimDoJogoEvent;
import com.crud.demo.models.DTO.BatalhaDTO;
import com.crud.demo.models.DTO.DialogoDTO;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.services.BatalhaServiceImpl;
import com.crud.demo.websocket.notificacoes.DialogoNotificacoes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FimDoJogoEventListener {

    private final BatalhaServiceImpl batalhaService;
    private final DialogoNotificacoes dialogoNotificacoes;

    @Transactional
    @EventListener
    public void finalizarJogo(FimDoJogoEvent fimDoJogoEvent) {
        log.info("[FimDoJogoEventListener] Recebido evento de fim de jogo: " + fimDoJogoEvent);

        BatalhaDTO batalha = batalhaService.getBatalhaById(fimDoJogoEvent.getIdBatalha());
        Long idGanhador = fimDoJogoEvent.getIdGanhador();
        log.info(String.format("[FimDoJogoEventListener] Finalizando batalha ID: %d. Ganhador ID: %d", fimDoJogoEvent.getIdBatalha(), idGanhador));

        batalha.setFinalizadoEm(LocalDateTime.now());
        log.info("[FimDoJogoEventListener] Batalha marcada como finalizada em " + batalha.getFinalizadoEm());

        batalha.getParticipantesBatalha().stream().forEach(personagem -> {
            if (personagem.getId().equals(idGanhador)) {
                personagem.setVencedor(true);
                log.info(String.format("[FimDoJogoEventListener] Personagem ID: %d marcado como VENCEDOR.", personagem.getId()));

            } else {
                personagem.setVencedor(false);
                log.info(String.format("[FimDoJogoEventListener] Personagem ID: %d marcado como PERDEDOR.", personagem.getId()));

            }
        });
        ParticipanteBatalha vencedor = batalha.getParticipantesBatalha().stream()
                .filter(participante -> participante.getVencedor() == true)
                .findFirst()
                .orElse(null);

        if (vencedor != null) {
            log.info("[FimDoJogoEventListener] Vencedor encontrado: " + vencedor.getPersonagem());

            String textoFimJogo = "A batalha chegou ao fim. O vencedor é: " + vencedor.getPersonagem();
            DialogoDTO dialogoDTO = new DialogoDTO(textoFimJogo);
            log.info("[FimDoJogoEventListener] Vencedor encontrado: " + vencedor.getPersonagem());

            dialogoNotificacoes.enviarDialogo(fimDoJogoEvent.getIdBatalha(), dialogoDTO);
        }

    }
}
