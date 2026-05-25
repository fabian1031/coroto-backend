package com.coroto.backend.auth;


import com.cohorte11.primerProyecto.model.Rol;
import com.cohorte11.primerProyecto.model.Usuario;
import com.cohorte11.primerProyecto.repository.UsuarioRepository;
import com.cohorte11.primerProyecto.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// AuthController expone los dos únicos endpoints públicos de la API:
// POST /auth/register → crea un usuario nuevo
// POST /auth/login    → valida credenciales y devuelve el token JWT
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // register: recibe los datos del nuevo usuario, hashea la contraseña
    // y guarda el usuario en la base de datos.
    // Si el email ya existe, retorna 400 Bad Request.
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El email ya está registrado"));
        }

        // Hashear la contraseña antes de guardar.
        // NUNCA se guarda la contraseña en texto plano.
        Usuario usuario = new Usuario(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNombre(),
                request.getRol() != null ? request.getRol() : Rol.CLIENTE
        );

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario registrado exitosamente",
                "email", usuario.getEmail(),
                "rol", usuario.getRol()
        ));
    }

    // login: recibe email y contraseña, delega la verificación al
    // AuthenticationManager, y si son correctas genera y devuelve el token.
    // Si las credenciales son incorrectas, Spring Security lanza una excepción
    // que retorna automáticamente 401 Unauthorized.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        // authenticate lanza BadCredentialsException si las credenciales no coinciden.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Si llegamos aquí, la autenticación fue exitosa.
        // getPrincipal() retorna el UserDetails del usuario autenticado.
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Generar el token JWT con los datos del usuario.
        String token = jwtUtil.generateToken(usuario);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", usuario.getEmail(),
                "rol", usuario.getRol(),
                "nombre", usuario.getNombre()
        ));
    }
}