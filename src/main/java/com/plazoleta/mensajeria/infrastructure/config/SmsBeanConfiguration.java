package com.plazoleta.mensajeria.infrastructure.config;

import com.plazoleta.mensajeria.application.handler.SmsHandler;
import com.plazoleta.mensajeria.application.handler.SmsHandlerImpl;
import com.plazoleta.mensajeria.application.usecase.SmsUseCase;
import com.plazoleta.mensajeria.domain.api.SmsServicePort;
import com.plazoleta.mensajeria.domain.spi.SmsSenderPort;
import com.plazoleta.mensajeria.infrastructure.output.sms.adapter.SmsSimuladoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsBeanConfiguration {

    @Bean
    public SmsSenderPort smsSenderPort() {
        return new SmsSimuladoAdapter();
    }

    @Bean
    public SmsServicePort smsServicePort(SmsSenderPort smsSenderPort) {
        return new SmsUseCase(smsSenderPort);
    }

    @Bean
    public SmsHandler smsHandler(SmsServicePort smsServicePort) {
        return new SmsHandlerImpl(smsServicePort);
    }
}