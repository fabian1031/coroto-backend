package com.coroto.backend.model;

import com.coroto.backend.model.enums.Rol;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "No debe quedar vacío")
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

    @Column(nullable = false)
    private Boolean activo;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<>();

    // CONSTRUCTORES

    public Usuario() {
        this.rol = Rol.CLIENTE;
        this.activo = true;
    }

    public Usuario(
            Long id,
            String nombre,
            String apellido,
            String email,
            String tipo_documento,
            String numero_documento,
            Rol rol, Boolean activo,
            List<Pedido> pedidos
    ){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.tipo_documento = tipo_documento;
        this.numero_documento = numero_documento;
        this.rol = rol;
        this.activo = activo;
        this.pedidos = pedidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> ordenes) {
        this.pedidos = ordenes;
    }
}