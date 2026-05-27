package com.coroto.backend.service;

import com.coroto.backend.DTO.OrdenRequestDTO;
import com.coroto.backend.DTO.OrdenResponseDTO;
import com.coroto.backend.model.Orden;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.repository.OrdenRepository;
import com.coroto.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public OrdenService(
            OrdenRepository ordenRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<OrdenResponseDTO> findAll() {
        return ordenRepository.findAll()
                .stream()
                .map(OrdenResponseDTO::desde)
                .collect(Collectors.toList());
    }

    public OrdenResponseDTO findById(Long id) {
        return ordenRepository.findById(id)
                .map(OrdenResponseDTO::desde)
                .orElse(null);
    }

    public OrdenResponseDTO save(OrdenRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));

        Orden orden = new Orden();
        orden.setFechaPedido(dto.getFechaPedido());
        orden.setEstado(dto.getEstado());
        orden.setEstadoPago(dto.getEstadoPago());
        orden.setDireccionEnvio(dto.getDireccionEnvio());
        orden.setCiudadEnvio(dto.getCiudadEnvio());
        orden.setUsuario(usuario);

        return OrdenResponseDTO.desde(ordenRepository.save(orden));
    }

    public OrdenResponseDTO update(Long id, OrdenRequestDTO dto) {
        Orden existente = ordenRepository.findById(id).orElse(null);
        if (existente == null) return null;

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));

        existente.setFechaPedido(dto.getFechaPedido());
        existente.setEstado(dto.getEstado());
        existente.setEstadoPago(dto.getEstadoPago());
        existente.setDireccionEnvio(dto.getDireccionEnvio());
        existente.setCiudadEnvio(dto.getCiudadEnvio());
        existente.setUsuario(usuario);

        return OrdenResponseDTO.desde(ordenRepository.save(existente));
    }

    public void delete(Long id) {
        ordenRepository.deleteById(id);
    }
}
