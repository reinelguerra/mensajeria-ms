package com.plazoleta.mensajeria.infrastructure.input.rest.mapper;

import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;
import com.plazoleta.mensajeria.infrastructure.input.rest.dto.SmsRequestDto;
import com.plazoleta.mensajeria.infrastructure.input.rest.dto.SmsResponseDto;
import org.springframework.stereotype.Component;

@Component
public class SmsRestMapper {

    public Sms toModel(SmsRequestDto request) {
        return new Sms(request.celular(), request.mensaje());
    }

    public SmsResponseDto toResponse(ResultadoEnvio resultado) {
        return new SmsResponseDto(resultado.celular(), resultado.estado());
    }
}