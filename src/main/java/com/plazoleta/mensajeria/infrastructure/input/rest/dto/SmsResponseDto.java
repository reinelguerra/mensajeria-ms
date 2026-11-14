package com.plazoleta.mensajeria.infrastructure.input.rest.dto;

import com.plazoleta.mensajeria.domain.model.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado del envío")
public record SmsResponseDto(
        @Schema(example = "+573001234567") String celular,
        @Schema(description = "ENVIADO si salió por el proveedor, SIMULADO si solo quedó en el log",
                example = "SIMULADO") EstadoEnvio estado) {
}