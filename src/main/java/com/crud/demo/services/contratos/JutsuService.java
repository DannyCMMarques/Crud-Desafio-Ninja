package com.crud.demo.services.contratos;

import java.util.Map;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.DTO.JutsuDTO;

public interface JutsuService {

    Map<String, JutsuDTO> obterJutsusDisponiveis(EstatisticaDoJogadorEvent personagemEvent);

    void adicionarHistorico(EstatisticaDoJogadorEvent personagemEvent, JutsuDTO jutsuGasto);

    Jutsu getJutsuById(Long id);

    Jutsu getJutsuByTipo(String tipo);

}
