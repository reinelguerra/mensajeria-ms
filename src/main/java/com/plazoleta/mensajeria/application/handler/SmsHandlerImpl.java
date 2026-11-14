package com.plazoleta.mensajeria.application.handler;

import com.plazoleta.mensajeria.domain.api.SmsServicePort;
import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;

public class SmsHandlerImpl implements SmsHandler {

    private final SmsServicePort smsServicePort;

    public SmsHandlerImpl(SmsServicePort smsServicePort) {
        this.smsServicePort = smsServicePort;
    }

    @Override
    public ResultadoEnvio enviarSms(Sms sms) {
        return smsServicePort.enviarSms(sms);
    }
}