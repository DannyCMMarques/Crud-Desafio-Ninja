package com.crud.demo.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AlertaDTO {

    private String mensagem;

}
