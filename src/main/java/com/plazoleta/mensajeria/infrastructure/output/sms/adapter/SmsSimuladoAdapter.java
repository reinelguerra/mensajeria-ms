package com.plazoleta.mensajeria.infrastructure.output.sms.adapter;

import com.plazoleta.mensajeria.domain.model.EstadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;
import com.plazoleta.mensajeria.domain.spi.SmsSenderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Envío simulado: no sale ningún SMS, solo queda registrado en el log (con el celular enmascarado). */
public class SmsSimuladoAdapter implements SmsSenderPort {

    private static final Logger LOG = LoggerFactory.getLogger(SmsSimuladoAdapter.class);

    @Override
    public EstadoEnvio enviar(Sms sms) {
        LOG.info("[SMS SIMULADO] para {}: {}", enmascarar(sms.celular()), sms.mensaje());
        return EstadoEnvio.SIMULADO;
    }

    private static String enmascarar(String celular) {
        int desde = Math.max(0, celular.length() - 4);
        return "****" + celular.substring(desde);
    }
}