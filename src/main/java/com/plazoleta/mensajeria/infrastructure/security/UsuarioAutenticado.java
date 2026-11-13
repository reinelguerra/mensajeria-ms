package com.plazoleta.mensajeria.infrastructure.security;

import com.plazoleta.mensajeria.domain.model.RolUsuario;

public record UsuarioAutenticado(Long id, String correo, RolUsuario rol) {
}