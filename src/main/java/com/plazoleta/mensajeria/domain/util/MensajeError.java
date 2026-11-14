package com.plazoleta.mensajeria.domain.util;

public final class MensajeError {

    public static final String TOKEN_REQUERIDO = "Debes iniciar sesión con un token válido";
    public static final String SIN_PERMISO = "Tu rol no tiene permiso para esta acción";

    public static final String CELULAR_INVALIDO =
            "El celular es obligatorio y debe tener entre 10 y 12 dígitos, con + opcional al inicio";
    public static final String MENSAJE_OBLIGATORIO = "El mensaje es obligatorio";
    public static final String MENSAJE_MUY_LARGO = "El mensaje no puede superar los 300 caracteres";
    public static final String ENVIO_FALLIDO = "No se pudo enviar el SMS, intenta de nuevo más tarde";
    public static final String CUERPO_INVALIDO = "El cuerpo de la petición no es un JSON válido";
    private MensajeError() {
    }
}