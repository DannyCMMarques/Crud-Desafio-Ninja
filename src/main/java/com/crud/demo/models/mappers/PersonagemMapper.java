package com.crud.demo.models.mappers;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonagemMapper {

    private final JutsuMapper jutsuMapper;

    public Personagem toEntity(PersonagemDTO dto) {
        Map<String, Jutsu> mapaJutsus = dto.getJutsus() != null
            ? dto.getJutsus().entrySet().stream()
                .collect(Collectors.toMap(
                    entry -> entry.getKey(),
                    entry -> jutsuMapper.toEntity(entry.getValue())
                ))
            : Collections.emptyMap();

        return Personagem.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .idade(dto.getIdade())
                .aldeia(dto.getAldeia())
                .especialidade(dto.getEspecialidade())
                .jutsus(mapaJutsus)
                .build();
    }

    public PersonagemDTO toDto(Personagem personagem) {
        Map<String, Jutsu> jutsuMap = personagem.getJutsus();
        Map<String, com.crud.demo.models.DTO.JutsuDTO> dtoMap = jutsuMap != null
            ? jutsuMap.entrySet().stream()
                .collect(Collectors.toMap(
                    entry -> entry.getKey(),
                    entry -> jutsuMapper.toDto(entry.getValue())
                ))
            : Collections.emptyMap();

        return PersonagemDTO.builder()
                .id(personagem.getId())
                .nome(personagem.getNome())
                .idade(personagem.getIdade())
                .aldeia(personagem.getAldeia())
                .chakra(personagem.getChakra())
                .vida(personagem.getVida())
                .especialidade(personagem.getEspecialidade())
                .jutsus(dtoMap)
                .dataCriacao(personagem.getDataCriacao())
                .build();
    }

    public void preencherDados(Personagem destino, Personagem origem) {
        destino.setId(origem.getId());
        destino.setNome(origem.getNome());
        destino.setIdade(origem.getIdade());
        destino.setAldeia(origem.getAldeia());
        destino.setEspecialidade(origem.getEspecialidade());
        destino.setJutsus(origem.getJutsus());
        destino.setDataCriacao(origem.getDataCriacao());
    }
}
