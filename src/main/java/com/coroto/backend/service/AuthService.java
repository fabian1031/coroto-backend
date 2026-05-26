package com.coroto.backend.service;

import com.coroto.backend.DTO.auth.LoginRequestDTO;
import com.coroto.backend.DTO.auth.LoginResponseDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            // 1. Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 2. Obtener el usuario autenticado
            Usuario usuario = (Usuario) authentication.getPrincipal();

            // 3. Verificar si está activo (aunque Spring ya lo hace con isEnabled())
            if (!usuario.isEnabled()) {
                throw new DisabledException("Usuario desactivado");
            }

            // 4. Generar token
            String token = jwtUtil.generateToken(usuario);

            // 5. Retornar respuesta con más información útil
            return new LoginResponseDTO(
                    token,
                    usuario.getId(),
                    usuario.getEmail(),
                    usuario.getRol().name()
            );

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Email o contraseña incorrectos");
        } catch (DisabledException e) {
            throw new RuntimeException("Usuario desactivado. Contacte al administrador");
        }
    }
}