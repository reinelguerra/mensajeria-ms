package com.plazoleta.mensajeria.application.handler;

import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;

/** Puerto de entrada de la capa de aplicación: lo único que conoce el controller. */
public interface SmsHandler {

    ResultadoEnvio enviarSms(Sms sms);
}