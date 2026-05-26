package com.coroto.backend.model;

import com.coroto.backend.model.enums.EstadoOrden;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenItem> items = new ArrayList<>();

    public Orden() {}

    public Orden(LocalDateTime fecha, EstadoOrden estado, User user) {
        this.fecha = fecha;
        this.estado = estado;
        this.user = user;
    }

    public Long getId() { return id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }

    public User getCliente() { return user; }
    public void setCliente(User user) { this.user = user; }

    public List<OrdenItem> getItems() { return items; }
    public void setItems(List<OrdenItem> items) { this.items = items; }
}
