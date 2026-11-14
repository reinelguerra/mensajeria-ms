package com.plazoleta.mensajeria.application.usecase;

import com.plazoleta.mensajeria.domain.exception.EnvioFallidoException;
import com.plazoleta.mensajeria.domain.exception.ReglaDeNegocioException;
import com.plazoleta.mensajeria.domain.model.EstadoEnvio;
import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;
import com.plazoleta.mensajeria.domain.spi.SmsSenderPort;
import com.plazoleta.mensajeria.domain.util.MensajeError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsUseCaseTest {

    private static final String CELULAR = "+573001234567";
    private static final String MENSAJE = "Tu pedido esta listo. PIN: 1234";

    @Mock
    private SmsSenderPort smsSenderPort;

    private SmsUseCase useCase;

    @BeforeEach
    void preparar() {
        useCase = new SmsUseCase(smsSenderPort);
    }

    private void rechaza(String celular, String mensaje, String mensajeEsperado) {
        ReglaDeNegocioException e = assertThrows(ReglaDeNegocioException.class,
                () -> useCase.enviarSms(new Sms(celular, mensaje)));
        assertEquals(mensajeEsperado, e.getMessage());
        verifyNoInteractions(smsSenderPort);
    }

    @Test
    void enviaElSmsYDevuelveElEstadoDelProveedor() {
        Sms sms = new Sms(CELULAR, MENSAJE);
        when(smsSenderPort.enviar(sms)).thenReturn(EstadoEnvio.ENVIADO);

        ResultadoEnvio resultado = useCase.enviarSms(sms);

        assertEquals(new ResultadoEnvio(CELULAR, EstadoEnvio.ENVIADO), resultado);
    }

    @Test
    void aceptaUnCelularSinSignoMas() {
        Sms sms = new Sms("3001234567", MENSAJE);
        when(smsSenderPort.enviar(sms)).thenReturn(EstadoEnvio.SIMULADO);

        ResultadoEnvio resultado = useCase.enviarSms(sms);

        assertEquals(new ResultadoEnvio("3001234567", EstadoEnvio.SIMULADO), resultado);
    }

    @Test
    void rechazaUnCelularNulo() {
        rechaza(null, MENSAJE, MensajeError.CELULAR_INVALIDO);
    }

    @Test
    void rechazaUnCelularVacio() {
        rechaza("  ", MENSAJE, MensajeError.CELULAR_INVALIDO);
    }

    @Test
    void rechazaUnCelularConLetras() {
        rechaza("30012345ab", MENSAJE, MensajeError.CELULAR_INVALIDO);
    }

    @Test
    void rechazaUnCelularMuyCorto() {
        rechaza("+57300", MENSAJE, MensajeError.CELULAR_INVALIDO);
    }

    @Test
    void rechazaUnMensajeNulo() {
        rechaza(CELULAR, null, MensajeError.MENSAJE_OBLIGATORIO);
    }

    @Test
    void rechazaUnMensajeEnBlanco() {
        rechaza(CELULAR, "   ", MensajeError.MENSAJE_OBLIGATORIO);
    }

    @Test
    void rechazaUnMensajeDeMasDe300Caracteres() {
        rechaza(CELULAR, "a".repeat(301), MensajeError.MENSAJE_MUY_LARGO);
    }

    @Test
    void propagaElFalloDelProveedor() {
        Sms sms = new Sms(CELULAR, MENSAJE);
        when(smsSenderPort.enviar(sms)).thenThrow(new EnvioFallidoException(MensajeError.ENVIO_FALLIDO));

        EnvioFallidoException e = assertThrows(EnvioFallidoException.class, () -> useCase.enviarSms(sms));

        assertEquals(MensajeError.ENVIO_FALLIDO, e.getMessage());
    }
}