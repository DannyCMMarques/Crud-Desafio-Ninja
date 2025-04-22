package com.crud.demo.models.DTO;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta do login contendo o token JWT, o tempo de expiração, e informações adicionais")
public class LoginResponseDTO {

    @Schema(description = "Token JWT gerado após a autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "E-mail do usuário autenticado", example = "joao.silva@example.com")
    private String sub;

    @Schema(description = "Função do usuário no sistema", example = "USER")
    private String role;

    @Schema(description = "Data de criação do usuário no formato timestamp", example = "2025-04-20T04:17:26.173")
    private String createdAt;

    @Schema(description = "Timestamp de expiração do token (em segundos desde a época Unix)", example = "1617971234")
    private Date exp;

    @Schema(description = "Timestamp de criação do token (em segundos desde a época Unix)", example = "1617970334")
    private long iat;

}
