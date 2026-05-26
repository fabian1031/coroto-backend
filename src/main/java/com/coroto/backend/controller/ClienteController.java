package com.coroto.backend.controller;


import com.coroto.backend.model.User;
import com.coroto.backend.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<User>> obtenerTodos() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> obtenerPorId(@PathVariable Long id) {
        User user = clienteService.findById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> crear(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> actualizar(@PathVariable Long id,
                                           @Valid @RequestBody User datos) {
        User actualizado = clienteService.update(id, datos);
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