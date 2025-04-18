package com.crud.demo.models.mappers;

import java.util.Collections;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonagemMapper {

    private final JutsuMapper jutsuMapper;

    public Personagem toEntity(PersonagemDTO dto) {
        return Personagem.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .idade(dto.getIdade())
                .aldeia(dto.getAldeia())
                .chakra(dto.getChakra())
                .jutsus(jutsuMapper.toEntityList(
                        Optional.ofNullable(dto.getJutsus()).orElse(Collections.emptyList())))
                .build();
    }

    public PersonagemDTO toDto(Personagem personagem) {
        return new PersonagemDTO(
                personagem.getId(),
                personagem.getNome(),
                personagem.getIdade(),
                personagem.getAldeia(),
                jutsuMapper.toDtoList(personagem.getJutsus()),
                personagem.getChakra());
    }

    public void preencherDados(Personagem destino, Personagem origem) {
        destino.setId(origem.getId());
        destino.setNome(origem.getNome());
        destino.setIdade(origem.getIdade());
        destino.setAldeia(origem.getAldeia());
        destino.setChakra(origem.getChakra());
        destino.setJutsus(origem.getJutsus());
    }
}
