package com.coroto.backend.DTO.auth;

public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String email;
    private String rol;

    public LoginResponseDTO(String token, Long userId, String email, String rol) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}