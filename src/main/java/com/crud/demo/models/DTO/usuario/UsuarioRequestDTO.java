package com.crud.demo.models.DTO.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO para registro de usuário, contendo informações necessárias para criação de um novo usuário")
public class UsuarioRequestDTO {
    @Schema(description = "Nome do usuário ", example = "João Silva")
    private String nome;
    @Schema(description = "Endereço de e-mail do usuário ", example = "joao.silva@example.com")
    private String email;
    @Schema(description = "Senha do usuário", example = "senhaSecreta123")
    private String senha;
}
