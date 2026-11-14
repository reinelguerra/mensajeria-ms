package com.plazoleta.mensajeria.infrastructure.exception;

import com.plazoleta.mensajeria.domain.exception.EnvioFallidoException;
import com.plazoleta.mensajeria.domain.exception.ReglaDeNegocioException;
import com.plazoleta.mensajeria.domain.util.MensajeError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<ErrorResponse> reglaDeNegocio(ReglaDeNegocioException e) {
        return construir(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EnvioFallidoException.class)
    public ResponseEntity<ErrorResponse> envioFallido(EnvioFallidoException e) {
        return construir(HttpStatus.BAD_GATEWAY, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException e) {
        return construir(HttpStatus.BAD_REQUEST, MensajeError.CUERPO_INVALIDO);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(new ErrorResponse(mensaje, status.value()));
    }
}