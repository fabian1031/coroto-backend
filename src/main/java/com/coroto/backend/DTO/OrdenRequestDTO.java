package com.coroto.backend.DTO;

import com.coroto.backend.model.enums.EstadoPedido;

import java.time.LocalDateTime;

public class OrdenRequestDTO {

    private LocalDateTime fecha;
    private EstadoPedido estado;
    private Long clienteId;

    public OrdenRequestDTO() {}

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}