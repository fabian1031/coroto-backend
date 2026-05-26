package com.coroto.backend.repository;

import com.coroto.backend.model.UsuarioABorrar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioABorrar, Long> {

    // Spring Data deriva la consulta del nombre del méodo:
    // SELECT * FROM usuarios WHERE email = ?
    Optional<UsuarioABorrar> findByEmail(String email);
}