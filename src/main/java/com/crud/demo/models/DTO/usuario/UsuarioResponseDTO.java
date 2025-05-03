package com.crud.demo.models.DTO.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UsuarioResponseDTO {
    private Long id;
    @Schema(description = "Nome do usuário ", example = "João Silva")
    private String nome;
    @Schema(description = "Endereço de e-mail do usuário ", example = "joao.silva@example.com")
    private String email;

}
