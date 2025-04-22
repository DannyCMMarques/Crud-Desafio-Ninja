package com.crud.demo.models.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registro de usuário, contendo informações necessárias para criação de um novo usuário")
public class RegistroUsuarioDTO {

    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @NotNull(message = "E-mail é obrigatório")
    @Schema(description = "Endereço de e-mail do usuário", example = "joao.silva@example.com")
    private String email;

    @NotNull(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "senhaSecreta123")
    private String senha;
}
