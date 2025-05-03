package com.crud.demo.listerners;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.crud.demo.events.BatalhaStartEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Batalha;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.repositories.JutsuRepository;
import com.crud.demo.services.EstatisticasDoJogadorStore;
import com.crud.demo.services.ParticipanteBatalhaServiceImpl;
import com.crud.demo.validators.BatalhaValidators;
import com.crud.demo.websocket.notificacoes.EstatisticasNotificacoes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatalhaStartListener {

    private static final Logger log = LoggerFactory.getLogger(BatalhaStartListener.class);

    private final EstatisticasDoJogadorStore store;
    private final EstatisticasNotificacoes estatisticasNotificacoes;
    private final JutsuRepository jutsuRepository;
    private final PersonagemMapper personagemMapper;
    private final ParticipanteBatalhaServiceImpl participanteBatalhaService;
    private final BatalhaValidators batalhaValidators;

    @EventListener
    @Transactional
    public void iniciarBatalha(BatalhaStartEvent batalhaStartEvento) {

        Long idBatalha = batalhaStartEvento.getIdBatalha();
        log.info("🡆 iniciarBatalha2 chamado com idBatalha={}", idBatalha);
        Batalha batalha = batalhaValidators.validarExistencia(idBatalha);
        log.debug("batalha adicionada com sucesso {}", batalha.getId());
        batalha.setCriadoEm(LocalDateTime.now());
        log.debug("Novo horário de criação adicionado com sucesso");
        List<PersonagemResponseDTO> personagensDTO = participanteBatalhaService.getPersonagemByBatalhaId(idBatalha);

        log.debug("Personagens para batalha {}: {}", idBatalha, personagensDTO);

        List<EstatisticaDoJogadorEvent> estatisticasIniciais = personagensDTO.stream()
                .map(PersonagemRequestDTO -> new EstatisticaDoJogadorEvent(
                personagemMapper.toEntity(PersonagemRequestDTO),
                new HashMap<>(),
                100,
                5.00,
                jutsuRepository.findMapByCategoria(PersonagemRequestDTO.getEspecialidade())))
                .collect(Collectors.toList());
        log.info("Estatísticas iniciais para batalha {}: count={} ", idBatalha, estatisticasIniciais.size());

        store.iniciarBatalha(idBatalha, estatisticasIniciais);
        log.debug("Estatísticas atuais em store: {}", estatisticasIniciais);

        List<EstatisticaDoJogadorEvent> estatisticas = store.getEstatisticas(idBatalha);
        estatisticasNotificacoes.enviarEstatisticas(idBatalha, estatisticas);
        log.info("Notificações de estatísticas enviadas para batalha {}", idBatalha);

    }
}
