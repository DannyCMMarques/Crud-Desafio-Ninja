package com.crud.demo.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.DTO.batalha.BatalhaRequestDTO;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;

@Mapper(
    componentModel = "spring",
    uses = ParticipanteBatalhaMapper.class
)
public interface BatalhaMapper {

    @Mapping(source = "participantes", target = "participantesBatalha")
    BatalhaResponseDTO toDto(Batalha batalha);

    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "participantes", ignore = true)
    Batalha toEntity(BatalhaRequestDTO dto);

    @Mapping(source = "participantesBatalha", target = "participantes")
    Batalha toEntity(BatalhaResponseDTO dto);
}
