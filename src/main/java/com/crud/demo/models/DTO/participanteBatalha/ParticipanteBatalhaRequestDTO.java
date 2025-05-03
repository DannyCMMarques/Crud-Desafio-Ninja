package com.crud.demo.models.DTO.participanteBatalha;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipanteBatalhaRequestDTO {
    

    @Schema(description = "Id da batalha ", example = "1")
    @NotNull
    private Long batalha;

    @Schema(description = "Nome do usuário participante", example = "Naruto Uzumaki", required = true)
    @NotNull
    private String nomeUsuario;

    @Schema(description = "Nome do personagem escolhido pelo participante", example = "Kakashi Hatake", required = true)
    @NotNull
    private String personagem;

    @Schema(description = "Ordem do jogador (quem joga primeiro)", example = "1", required = true)
    @NotNull
    private Integer playerOrder;

    @Schema(description = "Indica se o participante foi vencedor", example = "true")
    private Boolean vencedor;
}
