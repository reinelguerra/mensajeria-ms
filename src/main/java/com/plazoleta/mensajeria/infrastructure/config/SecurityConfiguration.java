package com.plazoleta.mensajeria.infrastructure.config;

import com.plazoleta.mensajeria.domain.util.MensajeError;
import com.plazoleta.mensajeria.infrastructure.security.JwtAuthenticationFilter;
import com.plazoleta.mensajeria.infrastructure.security.JwtTokenParser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfiguration {

    @Bean
    public JwtTokenParser jwtTokenParser(@Value("${jwt.secret}") String secreto) {
        return new JwtTokenParser(secreto);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenParser jwtTokenParser)
            throws Exception {
        PathPatternRequestMatcher.Builder rutas = PathPatternRequestMatcher.withDefaults();
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(peticiones -> peticiones
                        .requestMatchers(
                                rutas.matcher("/swagger-ui/**"),
                                rutas.matcher("/swagger-ui.html"),
                                rutas.matcher("/v3/api-docs/**")).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenParser),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(errores -> errores
                        .authenticationEntryPoint((request, response, ex) -> escribirError(response,
                                HttpServletResponse.SC_UNAUTHORIZED, MensajeError.TOKEN_REQUERIDO))
                        .accessDeniedHandler((request, response, ex) -> escribirError(response,
                                HttpServletResponse.SC_FORBIDDEN, MensajeError.SIN_PERMISO)));
        return http.build();
    }

    private static void escribirError(HttpServletResponse response, int status, String mensaje)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"mensaje\":\"" + mensaje + "\",\"status\":" + status + "}");
    }
}