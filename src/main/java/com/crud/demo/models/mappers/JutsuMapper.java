package com.crud.demo.models.mappers;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.DTO.jutsu.JutsuRequestDTO;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;

@Mapper(componentModel = "spring")
public interface JutsuMapper {

        @Mapping(source = "id", target = "id")
        JutsuResponseDTO toDto(Jutsu jutsu);

        @Mapping(source = "id", target = "id")
        Jutsu toEntity(JutsuResponseDTO dto);

        @Mapping(target = "id", ignore = true)
        Jutsu toEntity(JutsuRequestDTO dto);

        @Named("mapToDto")
        default Map<String, JutsuResponseDTO> mapToDto(Map<String, Jutsu> jutsus) {
                if (jutsus == null)
                        return Collections.emptyMap();

                return jutsus.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> toDto(entry.getValue())));
        }

        @Named("mapToEntity")
        default Map<String, Jutsu> mapToEntity(Map<String, JutsuResponseDTO> dtoMap) {
                if (dtoMap == null)
                        return Collections.emptyMap();

                return dtoMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> toEntity(entry.getValue())));
        }

}
