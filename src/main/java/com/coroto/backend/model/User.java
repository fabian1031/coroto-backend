package com.coroto.backend.model;

import com.coroto.backend.model.enums.Rol;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "No debe quedar vacio")
    @Email(message = "El email debe tener formato valido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "No debe quedar vacio")
    @Column(nullable = false)
    private String tipo_documento;

    @NotBlank(message = "No debe quedar vacio")
    @Column(nullable = false, unique = true)
    private String numero_documento;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @JsonManagedReference
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Orden> ordenes = new ArrayList<>();

    public User() {
        this.rol = Rol.CLIENTE;
    }

    public User(String nombre, String email, List<Orden> ordenes) {
        this.nombre = nombre;
        this.email = email;
        this.ordenes = ordenes;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}