package com.crud.demo.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;

@Mapper(componentModel = "spring", uses = JutsuMapper.class)
public interface PersonagemMapper {


    @Mapping(source = "id",       target = "id")
    @Mapping(target = "jutsus", source = "jutsus", qualifiedByName = "mapToDto")
    PersonagemResponseDTO toDto(Personagem personagem);
  
    @Mapping(target = "jutsus", source = "jutsus", qualifiedByName = "mapToDto")
    PersonagemRequestDTO toRequestDto(Personagem personagem);

    @Mapping(target = "id",       ignore = true)
    @Mapping(source = "jutsus", target = "jutsus", qualifiedByName = "mapToEntity")
    Personagem toEntity(PersonagemRequestDTO dto);

    @Mapping(source = "id",       target = "id")
    Personagem toEntity(PersonagemResponseDTO dto);

}
