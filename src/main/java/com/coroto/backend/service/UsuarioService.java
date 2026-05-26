package com.coroto.backend.service;

import com.coroto.backend.auth.RegisterRequestDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario crearUsuario(RegisterRequestDTO registerRequest) {

        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario usuarioNuevo = registerRequest.toEntity();
        usuarioNuevo.setPasswordEncriptado(passwordEncoder.encode(registerRequest.getPassword()));

        return usuarioRepository.save(usuarioNuevo);
    }
}
