package com.coroto.backend.controller;


import com.coroto.backend.model.Usuario;
import com.coroto.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {

        Usuario usuario = usuarioService.findById(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario datos
    ) {

        Usuario actualizado =
                usuarioService.update(id, datos);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        usuarioService.delete(id);

        return ResponseEntity.noContent().build();
    }
}


//@RestController
//@RequestMapping("/usuarios")
//public class usuarioController {
//
//    private final usuarioService usuarioService;
//
//    @Autowired
//    public usuarioController(usuarioService usuarioService) {
//        this.usuarioService = usuarioService;
//    }
//
//    @GetMapping
//    public List<usuario> obtenerTodos() {
//        return usuarioService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public usuario obtenerPorId(@PathVariable Long id) {
//        return usuarioService.findById(id);
//    }
//
//    @PostMapping
//    public usuario crear(@RequestBody usuario usuario) {
//        return usuarioService.save(usuario);
//    }
//
//    @PutMapping("/{id}")
//    public usuario actualizar(@PathVariable Long id, @RequestBody usuario datos) {
//        return usuarioService.update(id, datos);
//    }
//
//    @DeleteMapping("/{id}")
//    public void eliminar(@PathVariable Long id) {
//        usuarioService.delete(id);
//    }
//}