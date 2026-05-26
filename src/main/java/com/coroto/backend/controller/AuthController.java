package com.coroto.backend.controller;

import com.coroto.backend.DTO.auth.LoginRequestDTO;
import com.coroto.backend.DTO.auth.LoginResponseDTO;
import com.coroto.backend.DTO.auth.RegisterRequestDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.security.JwtUtil;
import com.coroto.backend.service.AuthService;
import com.coroto.backend.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthService authService;  // ✅ Hacerlo final también
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(
            UsuarioService usuarioService,
            AuthService authService,  // ✅ AGREGAR AQUÍ
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.usuarioService = usuarioService;
        this.authService = authService;  // ✅ INICIALIZAR AQUÍ
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDTO request
    ) {

        Usuario usuario =
                usuarioService.crearUsuario(request);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario registrado exitosamente",
                "email", usuario.getEmail(),
                "rol", usuario.getRol(),
                "nombre", usuario.getNombre()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Error de autenticación",
                    "message", e.getMessage()
            ));
        }
    }
}