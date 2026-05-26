package com.coroto.backend.DTO;

import com.coroto.backend.model.enums.EstadoPago;
import com.coroto.backend.model.enums.EstadoPedido;

import java.time.LocalDateTime;

public class OrdenRequestDTO {

    private LocalDateTime fechaPedido;

    private EstadoPago estadoPago;

    private EstadoPedido estado;

    private String direccionEnvio;

    private String ciudadEnvio;

    private Long usuarioId;

    public OrdenRequestDTO() {}

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getCiudadEnvio() {
        return ciudadEnvio;
    }

    public void setCiudadEnvio(String ciudadEnvio) {
        this.ciudadEnvio = ciudadEnvio;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}