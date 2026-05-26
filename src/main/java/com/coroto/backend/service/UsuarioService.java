package com.coroto.backend.service;

import com.coroto.backend.DTO.auth.RegisterRequestDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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


    public List<Usuario> findAll() {return  usuarioRepository.findAll();}

    public Usuario update(Long id, @Valid Usuario datos) {

        Usuario existente = usuarioRepository.findById(id).orElse(null);

        if(existente == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());

        if(datos.getPasswordEncriptado() != null && !datos.getPasswordEncriptado().isEmpty()) {
            existente.setPasswordEncriptado(
                    passwordEncoder.encode(datos.getPasswordEncriptado())
            );
        }
        return usuarioRepository.save(existente);
    }

    public Usuario findById(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
    }

    public void delete(Long id) {usuarioRepository.deleteById(id);}

    public void FindEmail(String email) {usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));}
}
