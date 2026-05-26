package com.coroto.backend.DTO;

import com.coroto.backend.model.Orden;
import com.coroto.backend.model.enums.EstadoPago;
import com.coroto.backend.model.enums.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrdenResponseDTO {

    private Long id;

    private LocalDateTime fechaPedido;

    private EstadoPago estadoPago;

    private EstadoPedido estado;

    private String direccionEnvio;

    private String ciudadEnvio;

    private BigDecimal total;

    private Long usuarioId;

    private String usuarioNombre;

    public OrdenResponseDTO() {}

    public static OrdenResponseDTO desde(Orden orden) {

        OrdenResponseDTO dto = new OrdenResponseDTO();

        dto.id = orden.getId();
        dto.fechaPedido = orden.getFechaPedido();
        dto.estadoPago = orden.getEstadoPago();
        dto.estado = orden.getEstado();
        dto.direccionEnvio = orden.getDireccionEnvio();
        dto.ciudadEnvio = orden.getCiudadEnvio();
        dto.total = orden.getTotal();

        dto.usuarioId = orden.getUsuario().getId();
        dto.usuarioNombre = orden.getUsuario().getNombre();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public String getCiudadEnvio() {
        return ciudadEnvio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }
}