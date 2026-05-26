package com.coroto.backend.repository;

import com.coroto.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data deriva la consulta del nombre del método:
    // SELECT * FROM usuarios WHERE email = ?
    Optional<Usuario> findByEmail(String email);
}