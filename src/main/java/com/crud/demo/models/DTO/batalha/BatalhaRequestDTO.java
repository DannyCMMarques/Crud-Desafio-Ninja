package com.crud.demo.models.DTO.batalha;

import java.time.LocalDateTime;

import com.crud.demo.models.enuns.StatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatalhaRequestDTO {

    @Schema(description = "Data e hora de criação da batalha (definido automaticamente pelo sistema)", example = "2025-04-27T15:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime criadoEm;

    @Schema(description = "Data e hora de finalização da batalha (definido pelo sistema)", example = "2025-04-27T16:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime finalizadoEm;

    @Schema(description = "Status da batalha (definido automaticamente como NAO_INICIADA)", example = "NAO_INICIADA", accessMode = Schema.AccessMode.READ_ONLY)
    private StatusEnum status;
}