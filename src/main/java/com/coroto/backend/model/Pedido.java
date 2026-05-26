package com.coroto.backend.model;

import com.coroto.backend.model.enums.EstadoPedido;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaDePedido;

    @Column
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Column(nullable = false)
    private String ciudadEnvio;

    @Column(nullable = false)
    private String direccionEnvio;

    @Column(nullable = false)
    private double total;

    @JsonManagedReference
    @ManyToOne()
    private Usuario usuario;

    public Pedido() {}

    public Pedido(
            Long id,
            LocalDateTime fechaDePedido,
            EstadoPedido estado,
            String ciudadEnvio,
            String direccionEnvio,
            double total,
            Usuario usuario
    ) {
        this.id = id;
        this.fechaDePedido = fechaDePedido;
        this.estado = estado;
        this.ciudadEnvio = ciudadEnvio;
        this.direccionEnvio = direccionEnvio;
        this.total = total;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaDePedido() {
        return fechaDePedido;
    }

    public void setFechaDePedido(LocalDateTime fechaDePedido) {
        this.fechaDePedido = fechaDePedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getCiudadEnvio() {
        return ciudadEnvio;
    }

    public void setCiudadEnvio(String ciudadEnvio) {
        this.ciudadEnvio = ciudadEnvio;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
