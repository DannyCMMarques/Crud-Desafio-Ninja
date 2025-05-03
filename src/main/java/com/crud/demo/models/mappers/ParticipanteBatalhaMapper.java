package com.crud.demo.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaRequestDTO;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaResponseDTO;
import com.crud.demo.models.mappers.helpers.ParticipanteBatalhaResolver;

@Mapper(componentModel = "spring", uses = ParticipanteBatalhaResolver.class)
public interface ParticipanteBatalhaMapper {

    @Mapping(source = "batalha.id", target = "batalha")
    @Mapping(source = "usuario.nome", target = "nomeUsuario")
    @Mapping(source = "personagem.nome", target = "personagem")
    @Mapping(source = "playerOrder", target = "playerOrder")
    @Mapping(source = "vencedor", target = "vencedor")
    ParticipanteBatalhaResponseDTO toDto(ParticipanteBatalha participante);



    @Mapping(target = "id",             ignore = true)
    @Mapping(source = "batalha", target = "batalha", qualifiedByName = "mapBatalhaById")
    @Mapping(source = "nomeUsuario", target = "usuario", qualifiedByName = "mapUsuarioByNome")
    @Mapping(source = "personagem", target = "personagem", qualifiedByName = "mapPersonagemByNome")
    @Mapping(source = "playerOrder", target = "playerOrder")
    @Mapping(source = "vencedor", target = "vencedor")
    ParticipanteBatalha toEntity(ParticipanteBatalhaRequestDTO dto);

    
     @Mapping(source = "id",             target = "id")
     @Mapping(source = "batalha",        target = "batalha",    qualifiedByName = "mapBatalhaById")
     @Mapping(source = "nomeUsuario",    target = "usuario",    qualifiedByName = "mapUsuarioByNome")
     @Mapping(source = "personagem",     target = "personagem", qualifiedByName = "mapPersonagemByNome")
     @Mapping(source = "playerOrder",    target = "playerOrder")
     @Mapping(source = "vencedor",       target = "vencedor")
     ParticipanteBatalha toEntity(ParticipanteBatalhaResponseDTO dto);
}
