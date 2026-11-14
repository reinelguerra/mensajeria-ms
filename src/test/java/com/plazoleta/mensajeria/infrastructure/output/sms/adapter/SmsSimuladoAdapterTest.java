package com.plazoleta.mensajeria.infrastructure.output.sms.adapter;

import com.plazoleta.mensajeria.domain.model.EstadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmsSimuladoAdapterTest {

    @Test
    void enviar_devuelveSimuladoSinLanzarErrores() {
        SmsSimuladoAdapter adapter = new SmsSimuladoAdapter();

        EstadoEnvio estado = adapter.enviar(new Sms("+573001234567", "Tu pedido esta listo. PIN: 1234"));

        assertEquals(EstadoEnvio.SIMULADO, estado);
    }
}