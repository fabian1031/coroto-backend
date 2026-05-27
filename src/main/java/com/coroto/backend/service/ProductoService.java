package com.coroto.backend.service;

import com.coroto.backend.model.Producto;
import com.coroto.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto update(Long id, Producto datos) {
        Producto existente = productoRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setCategoria(datos.getCategoria());
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setCantidad(datos.getCantidad());
        existente.setImageUrl(datos.getImageUrl());
        existente.setActivo(datos.getActivo());
        return productoRepository.save(existente);
    }

    public void delete(Long id) {
        productoRepository.deleteById(id);
    }
}
