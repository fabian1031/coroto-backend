package com.coroto.backend.service;

import com.coroto.backend.DTO.UsuarioResponseDTO;
import com.coroto.backend.DTO.UsuarioUpdateRequestDTO;
import com.coroto.backend.DTO.auth.RegisterRequestDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::desde)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponseDTO::desde)
                .orElse(null);
    }

    public UsuarioResponseDTO update(Long id, UsuarioUpdateRequestDTO datos) {
        Usuario existente = usuarioRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());

        if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
            existente.setPasswordEncriptado(passwordEncoder.encode(datos.getPassword()));
        }
        return UsuarioResponseDTO.desde(usuarioRepository.save(existente));
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}
