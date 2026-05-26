package com.coroto.backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrdenItemRequestDTO {


    private Long ordenId;
    private Long productoId;

    @NotNull(message = "No queda vacio")
    @Min(value = 1, message = "Cantidad min es 1")
    private Integer cantidad;
    private BigDecimal precioUnitario;

    public OrdenItemRequestDTO() {}

    public Long getOrdenId() { return ordenId; }
    public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}
