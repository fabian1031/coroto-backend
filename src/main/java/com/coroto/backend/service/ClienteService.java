package com.coroto.backend.service;

import com.coroto.backend.model.User;
import com.coroto.backend.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<User> findAll() {
        return clienteRepository.findAll();
    }

    public User findById(Long id){
        return clienteRepository.findById(id).orElse(null);
    }

    public User save(User user){
        return clienteRepository.save(user);
    }

    public User update(Long id, @Valid User datos) {
        User existente = clienteRepository.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setNombre(datos.getNombre());
        existente.setEmail(datos.getEmail());
        existente.setRol(datos.getRol());
        return clienteRepository.save(existente);
    }

    public void delete(Long id){
        clienteRepository.deleteById(id);
    }
}
