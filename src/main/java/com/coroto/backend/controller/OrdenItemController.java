package com.coroto.backend.controller;

import com.coroto.backend.DTO.OrdenItemRequestDTO;
import com.coroto.backend.DTO.OrdenItemResponseDTO;
import com.coroto.backend.service.OrdenItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/detalle_pedido")
public class OrdenItemController {

    private final OrdenItemService ordenItemService;

    @Autowired
    public OrdenItemController(OrdenItemService ordenItemService) {
        this.ordenItemService = ordenItemService;
    }

    @GetMapping
    public ResponseEntity<List<OrdenItemResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(ordenItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenItemResponseDTO> obtenerPorId(@PathVariable Long id) {
        OrdenItemResponseDTO item = ordenItemService.findById(id);
        if (item == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<OrdenItemResponseDTO> crear(@Valid @RequestBody OrdenItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenItemService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenItemResponseDTO> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody OrdenItemRequestDTO dto) {
        OrdenItemResponseDTO actualizado = ordenItemService.update(id, dto);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
