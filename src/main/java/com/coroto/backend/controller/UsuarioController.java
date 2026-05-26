package com.coroto.backend.controller;


import com.coroto.backend.auth.RegisterRequestDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        usuarioService.crearUsuario(registerRequest)
                );
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = clienteService.findById(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id,
                                              @Valid @RequestBody Usuario datos) {
        Usuario actualizado = clienteService.update(id, datos);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

//@RestController
//@RequestMapping("/clientes")
//public class ClienteController {
//
//    private final ClienteService clienteService;
//
//    @Autowired
//    public ClienteController(ClienteService clienteService) {
//        this.clienteService = clienteService;
//    }
//
//    @GetMapping
//    public List<Cliente> obtenerTodos() {
//        return clienteService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Cliente obtenerPorId(@PathVariable Long id) {
//        return clienteService.findById(id);
//    }
//
//    @PostMapping
//    public Cliente crear(@RequestBody Cliente cliente) {
//        return clienteService.save(cliente);
//    }
//
//    @PutMapping("/{id}")
//    public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente datos) {
//        return clienteService.update(id, datos);
//    }
//
//    @DeleteMapping("/{id}")
//    public void eliminar(@PathVariable Long id) {
//        clienteService.delete(id);
//    }
//}