package com.plazoleta.mensajeria.domain.spi;

import com.plazoleta.mensajeria.domain.model.EstadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;

public interface SmsSenderPort {

    /** Envía el SMS y dice cómo quedó. Lanza EnvioFallidoException si el proveedor falla. */
    EstadoEnvio enviar(Sms sms);
}