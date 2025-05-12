package com.crud.demo.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
import com.crud.demo.models.Personagem;

@Mapper(componentModel = "spring", uses = JutsuMapper.class)
public interface PersonagemMapper {


    @Mapping(target = "jutsus", source = "jutsus", qualifiedByName = "mapToDto")
    PersonagemResponseDTO toDto(Personagem personagem);
  
    @Mapping(target = "jutsus", source = "jutsus", qualifiedByName = "mapToDto")
    PersonagemRequestDTO toRequestDto(Personagem personagem);

    @Mapping(target = "id",       ignore = true)
    @Mapping(source = "jutsus", target = "jutsus", qualifiedByName = "mapToEntity")
    Personagem toEntity(PersonagemRequestDTO dto);

    Personagem toEntity(PersonagemResponseDTO dto);

}
