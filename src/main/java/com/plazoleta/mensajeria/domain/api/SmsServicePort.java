package com.plazoleta.mensajeria.domain.api;

import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;

public interface SmsServicePort {

    ResultadoEnvio enviarSms(Sms sms);
}