package com.crud.demo.models.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.DTO.BatalhaDTO;
import com.crud.demo.models.DTO.BatalhaRequestDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatalhaMapper {


    public BatalhaDTO toDto(Batalha batalha) {
        if (batalha == null) {
            return null;
        }

        return BatalhaDTO.builder()
            .id(batalha.getId())
            .criadoEm(batalha.getCriadoEm())
            .finalizadoEm(batalha.getFinalizadoEm())
            .status(batalha.getStatus())
            .participantesBatalha(
                batalha.getParticipantes())
            .build();
    }

    public Batalha toEntity(BatalhaDTO dto) {
        if (dto == null) {
            return null;
        }

        Batalha.BatalhaBuilder builder = Batalha.builder()
            .id(dto.getId())
            .criadoEm(dto.getCriadoEm())
            .finalizadoEm(dto.getFinalizadoEm())
            .status(dto.getStatus());

        List<ParticipanteBatalha> participantes = dto.getParticipantesBatalha();

        return builder
            .participantes(participantes)
            .build();
    }

    public BatalhaDTO fromRequest(BatalhaRequestDTO request,
                                 List<ParticipanteBatalha> participantes) {
        return BatalhaDTO.builder()
            .id(null)
            .criadoEm(request.getCriadoEm())
            .finalizadoEm(request.getFinalizadoEm())
            .status(request.getStatus())
            .participantesBatalha(participantes)
            .build();
    }
}
