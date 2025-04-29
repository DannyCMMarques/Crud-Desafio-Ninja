package com.crud.demo.models.DTO;

import java.time.LocalDateTime;

import com.crud.demo.models.enuns.StatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatalhaRequestDTO {

    @Schema(description = "Data e hora de criação da batalha", example = "2025-04-27T15:30:00")
    private LocalDateTime criadoEm;

    @Schema(description = "Data e hora de finalização da batalha", example = "2025-04-27T16:00:00")
    private LocalDateTime finalizadoEm;

    @Schema(description = "Status da batalha", example = "NAO_INICIADA", required = true)
    @NotNull
    private StatusEnum status;
}
