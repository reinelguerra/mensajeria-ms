package com.plazoleta.mensajeria.infrastructure.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SMS a enviar")
public record SmsRequestDto(
        @Schema(description = "Celular de 10 a 12 dígitos, con + opcional al inicio",
                example = "+573001234567") String celular,
        @Schema(description = "Texto del mensaje (máximo 300 caracteres)",
                example = "Tu pedido esta listo. PIN: 1234") String mensaje) {
}