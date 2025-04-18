package com.crud.demo.models.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.DTO.JutsuDTO;

@Component
public class JutsuMapper {

    public JutsuDTO toDto(Jutsu jutsu) {
        JutsuDTO dto = new JutsuDTO();
        dto.setId(jutsu.getId());
        dto.setTipo(jutsu.getTipo());
        return dto;
    }

    public Jutsu toEntity(JutsuDTO dto) {
        return Jutsu.builder()
                .id(dto.getId())
                .tipo(dto.getTipo())
                .build();
    }

    public List<JutsuDTO> toDtoList(List<Jutsu> jutsus) {
        return jutsus.stream().map(this::toDto).toList();
    }

    public List<Jutsu> toEntityList(List<JutsuDTO> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}
