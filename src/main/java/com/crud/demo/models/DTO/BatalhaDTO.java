package com.crud.demo.models.DTO;

import java.time.LocalDateTime;
import java.util.List;

import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.enuns.StatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatalhaDTO {

    @Schema(description = "ID da batalha", example = "1")
    private Long id;

    @Schema(description = "Data e hora de criação da batalha", example = "2025-04-27T15:30:00")
    private LocalDateTime criadoEm;

    @Schema(description = "Data e hora de finalização da batalha", example = "2025-04-27T16:00:00")
    private LocalDateTime finalizadoEm;

    @Schema(description = "Status da batalha", example = "NAO_INICIADA", required = true)
    @NotNull
    private StatusEnum status;

    @Schema(description = "Lista de participantes da batalha", required = true)
    @NotNull
    private List<ParticipanteBatalha> participantesBatalha;
}
