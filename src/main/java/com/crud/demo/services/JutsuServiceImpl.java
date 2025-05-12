package com.crud.demo.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.crud.demo.Exceptions.jutsusExceptions.JutsuNaoEncontradoException;
import com.crud.demo.controllers.BatalhaWsController;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
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
    public Map<String, JutsuResponseDTO> obterJutsusDisponiveis(EstatisticaDoJogadorEvent personagemEvent) {
        Personagem personagem = personagemEvent.getPersonagem();
        Map<String, Jutsu> jutsusAcessiveis = jutsuRepository.findMapByCategoria(personagem.getEspecialidade());
        Map<String, JutsuResponseDTO> dtos = jutsuMapper.mapToDto(jutsusAcessiveis);
        Map<String, JutsuResponseDTO> disponiveis = new HashMap<>(dtos);
        disponiveis.keySet()
                .removeAll(personagemEvent.getHistoricoDeJutsus().keySet());
        return disponiveis;
    }

    @Override
    public void adicionarHistorico(EstatisticaDoJogadorEvent personagemEvent, JutsuResponseDTO jutsuGasto) {
        personagemEvent.getHistoricoDeJutsus().put(jutsuGasto.getTipo(), jutsuGasto);

    }

    @Override
    public JutsuResponseDTO getJutsuById(Long id) {
        log.info("Buscando jutsu com id: {}", id);
        Jutsu jutsu = jutsuRepository.findById(id).orElseThrow(() -> new JutsuNaoEncontradoException());
        log.info("Jutsu encontrado: {}", jutsu);
        JutsuResponseDTO jutsuResponseDTO = jutsuMapper.toDto(jutsu);
        log.info("JutsuResponseDTO: {}", jutsuResponseDTO);
        return jutsuResponseDTO;
    }

    @Override
    public JutsuResponseDTO getJutsuByTipo(String tipo) {
        log.info("Buscando jutsu com tipo: {}", tipo);
        Jutsu jutsu = jutsuRepository.findByTipo(tipo).orElseThrow(() -> new JutsuNaoEncontradoException());
        log.info("Jutsu encontrado: {}", jutsu);
        JutsuResponseDTO jutsuResponseDTO = jutsuMapper.toDto(jutsu);
        log.info("JutsuResponseDTO: {}", jutsuResponseDTO);
        return jutsuResponseDTO;
}
}