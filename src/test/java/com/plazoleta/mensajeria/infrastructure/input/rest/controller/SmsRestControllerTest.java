package com.plazoleta.mensajeria.infrastructure.input.rest.controller;

import com.plazoleta.mensajeria.application.handler.SmsHandler;
import com.plazoleta.mensajeria.domain.exception.EnvioFallidoException;
import com.plazoleta.mensajeria.domain.exception.ReglaDeNegocioException;
import com.plazoleta.mensajeria.domain.model.EstadoEnvio;
import com.plazoleta.mensajeria.domain.model.ResultadoEnvio;
import com.plazoleta.mensajeria.domain.model.Sms;
import com.plazoleta.mensajeria.domain.util.MensajeError;
import com.plazoleta.mensajeria.infrastructure.exception.GlobalExceptionHandler;
import com.plazoleta.mensajeria.infrastructure.input.rest.mapper.SmsRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SmsRestControllerTest {

    private static final String RUTA = "/api/v1/mensajeria/sms";
    private static final String CUERPO_VALIDO =
            "{\"celular\":\"+573001234567\",\"mensaje\":\"Tu pedido esta listo. PIN: 1234\"}";

    @Mock
    private SmsHandler smsHandler;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new SmsRestController(smsHandler, new SmsRestMapper()))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private ResultActions enviar(String cuerpo) throws Exception {
        return mockMvc.perform(post(RUTA).contentType(MediaType.APPLICATION_JSON).content(cuerpo));
    }

    @Test
    void enviarSms_conDatosValidos_devuelve200ConElEstado() throws Exception {
        when(smsHandler.enviarSms(new Sms("+573001234567", "Tu pedido esta listo. PIN: 1234")))
                .thenReturn(new ResultadoEnvio("+573001234567", EstadoEnvio.SIMULADO));

        enviar(CUERPO_VALIDO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.celular").value("+573001234567"))
                .andExpect(jsonPath("$.estado").value("SIMULADO"));

        verify(smsHandler).enviarSms(new Sms("+573001234567", "Tu pedido esta listo. PIN: 1234"));
    }

    @Test
    void enviarSms_cuandoElDominioRechaza_devuelve400ConLaRegla() throws Exception {
        when(smsHandler.enviarSms(new Sms("+573001234567", "Tu pedido esta listo. PIN: 1234")))
                .thenThrow(new ReglaDeNegocioException(MensajeError.CELULAR_INVALIDO));

        enviar(CUERPO_VALIDO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(MensajeError.CELULAR_INVALIDO))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void enviarSms_cuandoFallaElProveedor_devuelve502() throws Exception {
        when(smsHandler.enviarSms(new Sms("+573001234567", "Tu pedido esta listo. PIN: 1234")))
                .thenThrow(new EnvioFallidoException(MensajeError.ENVIO_FALLIDO));

        enviar(CUERPO_VALIDO)
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.mensaje").value(MensajeError.ENVIO_FALLIDO))
                .andExpect(jsonPath("$.status").value(502));
    }

    @Test
    void enviarSms_conJsonInvalido_devuelve400() throws Exception {
        enviar("{esto no es json")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(MensajeError.CUERPO_INVALIDO));
    }
}