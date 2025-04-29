package com.crud.demo.models.mappers;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.DTO.JutsuDTO;

@Component
public class JutsuMapper {

    public JutsuDTO toDto(Jutsu jutsu) {
        return JutsuDTO.builder()
                .id(jutsu.getId())
                .tipo(jutsu.getTipo())
                .dano(jutsu.getDano())
                .consumo_de_chakra(jutsu.getConsumo_de_chakra())
                .categoria(jutsu.getCategoria())
                .build();
    }

    public Jutsu toEntity(JutsuDTO dto) {
        return Jutsu.builder()
                .id(dto.getId())
                .tipo(dto.getTipo())
                .dano(dto.getDano())
                .consumo_de_chakra(dto.getConsumo_de_chakra())
                .categoria(dto.getCategoria())
                .build();
    }

    public Map<String, JutsuDTO> toDtoMap(Map<String, Jutsu> jutsus) {
        return jutsus.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toDto(entry.getValue())
                ));
    }

    public Map<String, Jutsu> toEntityMap(Map<String, JutsuDTO> dtos) {
        return dtos.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toEntity(entry.getValue())
                ));
    }
}
