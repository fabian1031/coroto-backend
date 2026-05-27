package com.coroto.backend.config;

import com.coroto.backend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @EnableWebSecurity: activa el módulo de seguridad web de Spring Security.
// Esta clase define el SecurityFilterChain: las reglas que determinan
// qué solicitudes pasan y cuáles se bloquean, y con qué condiciones.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtFilter jwtFilter,
                          AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Swagger: público durante el desarrollo.
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Endpoints de autenticación: públicos — son la puerta de entrada.
                        .requestMatchers("/auth/**").permitAll()

                        // Productos: GET para CLIENTE y ADMIN, modificaciones solo ADMIN.
                        .requestMatchers(HttpMethod.GET,    "/productos/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.POST,   "/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**").hasRole("ADMIN")

                        // Usuarios: GET para ADMIN y CLIENTE, modificaciones solo ADMIN.
                        .requestMatchers(HttpMethod.GET,    "/usuarios/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.POST,   "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMIN")

                        // Pedidos: GET para ambos, POST para ambos (CLIENTE puede crear su pedido),
                        // modificaciones y eliminación solo ADMIN.
                        .requestMatchers(HttpMethod.GET,    "/pedidos/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.POST,   "/pedidos/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT,    "/pedidos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pedidos/**").hasRole("ADMIN")

                        // Detalle de pedido: GET y POST para ambos, modificaciones solo ADMIN.
                        .requestMatchers(HttpMethod.GET,    "/detalle_pedido/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.POST,   "/detalle_pedido/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT,    "/detalle_pedido/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/detalle_pedido/**").hasRole("ADMIN")

                        // Todo lo demás requiere autenticación.
                        .anyRequest().authenticated()
                )

                // Registrar el AuthenticationProvider que usa la BD.
                .authenticationProvider(authenticationProvider)

                // Insertar JwtFilter antes del filtro estándar de usuario/contraseña.
                // Así, cada solicitud primero pasa por la validación del token JWT.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
