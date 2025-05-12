package com.crud.demo.models.DTO.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO para login de usuário, contendo as credenciais necessárias para autenticação")
public class LoginDTO {

    @Schema(description = "Endereço de e-mail do usuário para login", example = "joao.silva@example.com")
    private String email;

    @Schema(description = "Senha do usuário para login", example = "senhaSecreta123")
    private String senha;
}
