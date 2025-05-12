package com.crud.demo.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.models.DTO.usuario.UsuarioRequestDTO;
import com.crud.demo.models.DTO.usuario.UsuarioResponseDTO;
import com.crud.demo.models.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toDto(Usuario usuario);

    @Mapping(target = "senha",       ignore = true)
    @Mapping(target = "createdAt",   ignore = true)
    @Mapping(target = "updatedAt",   ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Usuario toEntity(UsuarioResponseDTO dto);

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "createdAt",   ignore = true)
    @Mapping(target = "updatedAt",   ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);
}

