package com.crud.demo.models.mappers;

import org.springframework.stereotype.Component;

import com.crud.demo.models.DTO.RegistroUsuarioDTO;
import com.crud.demo.models.Usuario;

@Component
public class UsuarioMapper {
    public RegistroUsuarioDTO toDto(Usuario usuario) {
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        return dto;
    }

    public Usuario toEntity(RegistroUsuarioDTO dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
    }
}
