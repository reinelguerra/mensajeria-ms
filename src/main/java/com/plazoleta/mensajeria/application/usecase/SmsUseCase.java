package com.plazoleta.mensajeria.application.usecase;

import com.plazoleta.mensajeria.domain.api.SmsServicePort;
import com.plazoleta.mensajeria.domain.exception.ReglaDeNegocioException;
import com.plazoleta.mensajeria.domain.model.EstadoEnvio;
import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;
import com.plazoleta.mensajeria.domain.spi.SmsSenderPort;
import com.plazoleta.mensajeria.domain.util.MensajeError;

public class SmsUseCase implements SmsServicePort {

    private static final String FORMATO_CELULAR = "^\\+?[0-9]{10,12}$";
    private static final int LONGITUD_MAXIMA_MENSAJE = 300;

    private final SmsSenderPort smsSenderPort;

    public SmsUseCase(SmsSenderPort smsSenderPort) {
        this.smsSenderPort = smsSenderPort;
    }

    @Override
    public ResultadoEnvio enviarSms(Sms sms) {
        validar(sms);
        EstadoEnvio estado = smsSenderPort.enviar(sms);
        return new ResultadoEnvio(sms.celular(), estado);
    }

    private void validar(Sms sms) {
        if (sms.celular() == null || !sms.celular().matches(FORMATO_CELULAR)) {
            throw new ReglaDeNegocioException(MensajeError.CELULAR_INVALIDO);
        }
        if (sms.mensaje() == null || sms.mensaje().isBlank()) {
            throw new ReglaDeNegocioException(MensajeError.MENSAJE_OBLIGATORIO);
        }
        if (sms.mensaje().length() > LONGITUD_MAXIMA_MENSAJE) {
            throw new ReglaDeNegocioException(MensajeError.MENSAJE_MUY_LARGO);
        }
    }
}