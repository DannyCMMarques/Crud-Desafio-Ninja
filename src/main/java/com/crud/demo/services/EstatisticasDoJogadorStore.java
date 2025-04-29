package com.crud.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.crud.demo.events.EstatisticaDoJogadorEvent;

@Component
public class EstatisticasDoJogadorStore {

    private final Map<Long, Map<Long, EstatisticaDoJogadorEvent>> store = new ConcurrentHashMap<>();

    public Map<Long, Map<Long, EstatisticaDoJogadorEvent>> iniciarBatalha(Long battleId, List<EstatisticaDoJogadorEvent> estatisticasIniciais) {
        Map<Long, EstatisticaDoJogadorEvent> mapJogadorEstatistica = new ConcurrentHashMap<>();
        for (EstatisticaDoJogadorEvent estatistica : estatisticasIniciais) {
            mapJogadorEstatistica.put(estatistica.getPersonagem().getId(), estatistica);
        }
        store.put(battleId, mapJogadorEstatistica);
        return store;
    }

    public EstatisticaDoJogadorEvent getEstatistica(Long battleId, Long personagemId) {
        return Optional.ofNullable(store.get(battleId))
                .map(m -> m.get(personagemId))
                .orElseThrow(() -> new IllegalArgumentException("Estatísticas não iniciadas para battleId="
                + battleId));
    }

    public List<EstatisticaDoJogadorEvent> getEstatisticas(Long battleId) {
        Map<Long, EstatisticaDoJogadorEvent> mapa = store.get(battleId);
        if (mapa == null) {
            throw new IllegalArgumentException(
                    "Estatísticas não iniciadas para battleId=" + battleId
            );
        }
        return new ArrayList<>(mapa.values());
    }

    public void updateEstatisticas(Long battleId,
            List<EstatisticaDoJogadorEvent> statsList) {
        for (EstatisticaDoJogadorEvent stats : statsList) {
            updateEstatistica(battleId, stats);
        }
    }

    public void updateEstatistica(Long battleId, EstatisticaDoJogadorEvent estatisticas) {
        Map<Long, EstatisticaDoJogadorEvent> mapa = store.get(battleId);
        if (mapa == null) {
            throw new IllegalArgumentException(
                    "BattleId não encontrado: " + battleId);
        }
        mapa.put(estatisticas.getPersonagem().getId(), estatisticas);
    }
}
