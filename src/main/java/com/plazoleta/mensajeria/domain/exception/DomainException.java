package com.plazoleta.mensajeria.domain.exception;

public abstract class DomainException extends RuntimeException {

    protected DomainException(String mensaje) {
        super(mensaje);
    }
}