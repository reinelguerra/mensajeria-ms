package com.plazoleta.mensajeria.infrastructure.input.rest;

public final class DocumentacionApi {

    public static final String NO_AUTENTICADO = "No autenticado: falta el token o no es válido";
    public static final String SIN_PERMISO = "Sin permiso: solo el rol EMPLEADO puede enviar SMS";
    public static final String DATOS_INVALIDOS = "Celular o mensaje inválidos";
    public static final String ENVIO_FALLIDO = "El proveedor de SMS falló al enviar el mensaje";

    private DocumentacionApi() {
    }
}