package com.coroto.backend.service;

import com.coroto.backend.DTO.auth.LoginRequestDTO;
import com.coroto.backend.DTO.auth.LoginResponseDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_credencialesValidas_retornaToken() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("juan@test.com");
        request.setPassword("Password1");

        Usuario usuario = new Usuario(
                "Juan", "Pérez", "juan@test.com", "hashedPassword", "CC", "12345678"
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(usuario)).thenReturn("jwt-token-generado");

        LoginResponseDTO resultado = authService.login(request);

        assertNotNull(resultado);
        assertEquals("jwt-token-generado", resultado.getToken());
        assertEquals("juan@test.com", resultado.getEmail());
        assertEquals("CLIENTE", resultado.getRol());
        verify(jwtUtil, times(1)).generateToken(usuario);
    }

    @Test
    void login_credencialesInvalidas_lanzaExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("juan@test.com");
        request.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Email o contraseña incorrectos", excepcion.getMessage());
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void login_usuarioDesactivado_lanzaExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("inactivo@test.com");
        request.setPassword("Password1");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("User is disabled"));

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Usuario desactivado. Contacte al administrador", excepcion.getMessage());
        verify(jwtUtil, never()).generateToken(any());
    }
}
