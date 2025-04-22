package com.crud.demo.models.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para login de usuário, contendo as credenciais necessárias para autenticação")
public class LoginDTO {

    @NotNull(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    @Schema(description = "Endereço de e-mail do usuário para login", example = "joao.silva@example.com")
    private String email;

    @NotNull(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário para login", example = "senhaSecreta123")
    private String senha;
}
