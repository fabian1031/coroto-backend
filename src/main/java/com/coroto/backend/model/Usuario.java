package com.coroto.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CollectionId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El email es el identificador único de login.
    @Column(unique = true, nullable = false)
    private String email;



    // La contraseña se guarda hasheada con BCrypt — nunca en texto plano.
    @Column(nullable = false)
    private String password;

    // El nombre del usuario para mostrar en respuestas.
    @Column(nullable = false)
    private String nombre;

    // el apellido para mostrar respuestas
    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String tipo_documento;

    @Column(nullable = false, unique = true)
    private String numero_documento;

    @Column (nullable = false)
    private String fecha_nacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;


    // El rol determina qué endpoints puede acceder este usuario.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Constructor vacío obligatorio para JPA.
    public Usuario() {}

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    // Constructor completo para crear usuarios programáticamente.
    public Usuario(String email, String password, String nombre, Rol rol) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipo_documento = tipo_documento;
        this.numero_documento = numero_documento;
        this.fecha_nacimiento = fecha_nacimiento;
        this.rol = rol;
    }

    // getAuthorities: Spring Security llama a este método para saber
    // qué roles tiene el usuario. Retorna una lista de GrantedAuthority.
    // SimpleGrantedAuthority("ROLE_ADMIN") es el formato que Spring espera.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    // getUsername: Spring Security usa este campo como identificador.
    // Aunque el mtodo se llama getUsername, nosotros usamos el email.
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Los tres métodos siguientes controlan el estado de la cuenta.
    // Retornan true para indicar que la cuenta está activa y habilitada.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }


    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}