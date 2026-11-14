package com.plazoleta.mensajeria.infrastructure.input.rest.controller;

import com.plazoleta.mensajeria.application.handler.SmsHandler;
import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.infrastructure.exception.ErrorResponse;
import com.plazoleta.mensajeria.infrastructure.input.rest.dto.SmsRequestDto;
import com.plazoleta.mensajeria.infrastructure.input.rest.dto.SmsResponseDto;
import com.plazoleta.mensajeria.infrastructure.input.rest.mapper.SmsRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.plazoleta.mensajeria.infrastructure.input.rest.DocumentacionApi.DATOS_INVALIDOS;
import static com.plazoleta.mensajeria.infrastructure.input.rest.DocumentacionApi.ENVIO_FALLIDO;
import static com.plazoleta.mensajeria.infrastructure.input.rest.DocumentacionApi.NO_AUTENTICADO;
import static com.plazoleta.mensajeria.infrastructure.input.rest.DocumentacionApi.SIN_PERMISO;

@RestController
@RequestMapping("/api/v1/mensajeria")
@Tag(name = "Mensajería", description = "Envío de mensajes SMS a los clientes")
@ApiResponse(responseCode = "401", description = NO_AUTENTICADO,
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public class SmsRestController {

    private final SmsHandler smsHandler;
    private final SmsRestMapper smsRestMapper;

    public SmsRestController(SmsHandler smsHandler, SmsRestMapper smsRestMapper) {
        this.smsHandler = smsHandler;
        this.smsRestMapper = smsRestMapper;
    }

    @Operation(summary = "Enviar un SMS",
            description = "Lo llama plazoleta-ms con el token del empleado que marca un pedido como Listo. "
                    + "Envía el texto que recibe (el PIN lo genera plazoleta-ms). Por defecto el envío es "
                    + "simulado: el estado de la respuesta es SIMULADO y el mensaje solo queda en el log.")
    @ApiResponse(responseCode = "400", description = DATOS_INVALIDOS,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = SIN_PERMISO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "502", description = ENVIO_FALLIDO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/sms")
    public ResponseEntity<SmsResponseDto> enviarSms(@RequestBody SmsRequestDto request) {
        ResultadoEnvio resultado = smsHandler.enviarSms(smsRestMapper.toModel(request));
        return ResponseEntity.ok(smsRestMapper.toResponse(resultado));
    }
}