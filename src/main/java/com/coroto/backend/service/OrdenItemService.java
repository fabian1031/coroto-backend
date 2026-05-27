package com.coroto.backend.service;

import com.coroto.backend.DTO.OrdenItemRequestDTO;
import com.coroto.backend.DTO.OrdenItemResponseDTO;
import com.coroto.backend.model.Orden;
import com.coroto.backend.model.OrdenItem;
import com.coroto.backend.model.Producto;
import com.coroto.backend.repository.OrdenItemRepository;
import com.coroto.backend.repository.OrdenRepository;
import com.coroto.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenItemService {

    private final OrdenItemRepository ordenItemRepository;
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public OrdenItemService(OrdenItemRepository ordenItemRepository,
                            OrdenRepository ordenRepository,
                            ProductoRepository productoRepository) {
        this.ordenItemRepository = ordenItemRepository;
        this.ordenRepository = ordenRepository;
        this.productoRepository = productoRepository;
    }

    public List<OrdenItemResponseDTO> findAll() {
        return ordenItemRepository.findAll()
                .stream()
                .map(OrdenItemResponseDTO::desde)
                .collect(Collectors.toList());
    }

    public OrdenItemResponseDTO findById(Long id) {
        OrdenItem item = ordenItemRepository.findById(id).orElse(null);
        if (item == null) return null;
        return OrdenItemResponseDTO.desde(item);
    }

    public OrdenItemResponseDTO save(OrdenItemRequestDTO dto) {
        Orden orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + dto.getOrdenId()));

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + dto.getProductoId()));

        OrdenItem item = new OrdenItem(dto.getCantidad(), dto.getPrecioUnitario(), orden, producto);
        return OrdenItemResponseDTO.desde(ordenItemRepository.save(item));
    }

    public OrdenItemResponseDTO update(Long id, OrdenItemRequestDTO dto) {
        OrdenItem existente = ordenItemRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setCantidad(dto.getCantidad());
        existente.setPrecioUnitario(dto.getPrecioUnitario());
        return OrdenItemResponseDTO.desde(ordenItemRepository.save(existente));
    }

    public void delete(Long id) {
        ordenItemRepository.deleteById(id);
    }
}
