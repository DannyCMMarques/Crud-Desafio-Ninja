package com.crud.demo.models.mappers;

import java.util.Collections;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.Personagem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonagemMappers {

    public Personagem toEntity(PersonagemDTO dto) {
        return Personagem.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .idade(dto.getIdade())
                .aldeia(dto.getAldeia())
                .jutsus(dto.getJutsus())
                .chakra(dto.getChakra())
                .build();
    }

    public PersonagemDTO toDto(Personagem pessoa) {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setIdade(pessoa.getIdade());
        dto.setAldeia(pessoa.getAldeia());
        dto.setJutsus(Optional.ofNullable(pessoa.getJutsus()).orElse(Collections.emptyList()));
        dto.setChakra(pessoa.getChakra());
        return dto;
    }

    public void preencherDados(Personagem personagem, Personagem personagemEntity) {
        personagem.setId(personagemEntity.getId());
        personagem.setNome(personagemEntity.getNome());
        personagem.setIdade(personagemEntity.getIdade());
        personagem.setAldeia(personagemEntity.getAldeia());
        personagem.setChakra(personagemEntity.getChakra());
        personagem.setJutsus(personagemEntity.getJutsus());
    }
}