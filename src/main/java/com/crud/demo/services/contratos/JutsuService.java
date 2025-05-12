package com.crud.demo.services.contratos;

import java.util.Map;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;

public interface JutsuService {

    Map<String, JutsuResponseDTO> obterJutsusDisponiveis(EstatisticaDoJogadorEvent personagemEvent);

    void adicionarHistorico(EstatisticaDoJogadorEvent personagemEvent, JutsuResponseDTO jutsuGasto);

    JutsuResponseDTO getJutsuById(Long id);

    JutsuResponseDTO getJutsuByTipo(String tipo);
}
