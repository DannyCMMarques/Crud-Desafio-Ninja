package com.crud.demo.services.acaoService;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.crud.demo.Exceptions.jutsusExceptions.JutsuNaoEncontradoException;
import com.crud.demo.controllers.BatalhaWsController;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.JutsuDTO;
import com.crud.demo.models.mappers.JutsuMapper;
import com.crud.demo.repositories.JutsuRepository;
import com.crud.demo.services.contratos.JutsuService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JutsuServiceImpl implements JutsuService {

    private static final Logger log = LoggerFactory.getLogger(BatalhaWsController.class);

    private final JutsuRepository jutsuRepository;
    private final JutsuMapper jutsuMapper;

    @Override
    public Map<String, JutsuDTO> obterJutsusDisponiveis(EstatisticaDoJogadorEvent personagemEvent) {
        Personagem personagem = personagemEvent.getPersonagem();
        Map<String, Jutsu> jutsusAcessiveis = jutsuRepository.findMapByCategoria(personagem.getEspecialidade());
        Map<String, JutsuDTO> dtos = jutsuMapper.toDtoMap(jutsusAcessiveis);
        Map<String, JutsuDTO> disponiveis = new HashMap<>(dtos);
        disponiveis.keySet()
                .removeAll(personagemEvent.getHistoricoDeJutsus().keySet());
        return disponiveis;
    }

    @Override
    public void adicionarHistorico(EstatisticaDoJogadorEvent personagemEvent, JutsuDTO jutsuGasto) {
        personagemEvent.getHistoricoDeJutsus().put(jutsuGasto.getTipo(), jutsuGasto);

    }

    @Override
    public Jutsu getJutsuById(Long id) {
        return jutsuRepository.findById(id).orElseThrow(() -> new JutsuNaoEncontradoException());
    }

    @Override
    public Jutsu getJutsuByTipo(String tipo) {

        return jutsuRepository.findByTipo(tipo).orElseThrow(() -> new JutsuNaoEncontradoException());
    }
}
