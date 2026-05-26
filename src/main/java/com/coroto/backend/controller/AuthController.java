package com.coroto.backend.controller;

import com.coroto.backend.auth.LoginRequestDTO;
import com.coroto.backend.auth.RegisterRequestDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.security.JwtUtil;
import com.coroto.backend.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(
            UsuarioService usuarioService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.usuarioService = usuarioService;
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
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDTO request
    ) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        Usuario usuario =
                (Usuario) authentication.getPrincipal();

        String token = jwtUtil.generateToken((UserDetails) usuario);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", usuario.getEmail(),
                "rol", usuario.getRol(),
                "nombre", usuario.getNombre()
        ));
    }
}