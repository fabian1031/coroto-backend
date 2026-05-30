package com.coroto.backend.service;

import com.coroto.backend.DTO.UsuarioResponseDTO;
import com.coroto.backend.DTO.UsuarioUpdateRequestDTO;
import com.coroto.backend.DTO.auth.RegisterRequestDTO;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearUsuario_emailNuevo_retornaUsuarioGuardado() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Juan", "Pérez", "juan@test.com", "Password1", "CC", "12345678"
        );

        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password1")).thenReturn("hashedPassword");

        Usuario usuarioGuardado = new Usuario(
                "Juan", "Pérez", "juan@test.com", "hashedPassword", "CC", "12345678"
        );
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        Usuario resultado = usuarioService.crearUsuario(request);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juan@test.com", resultado.getEmail());
        verify(passwordEncoder, times(1)).encode("Password1");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_emailYaExiste_lanzaExcepcion() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Juan", "Pérez", "juan@test.com", "Password1", "CC", "12345678"
        );

        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(new Usuario()));

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> usuarioService.crearUsuario(request));

        assertEquals("El usuario ya existe", excepcion.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void findAll_existenUsuarios_retornaLista() {
        Usuario u1 = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Usuario u2 = new Usuario("Luis", "Martínez", "luis@test.com", "hash", "CC", "22222222");

        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UsuarioResponseDTO> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void findById_idExistente_retornaDTO() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
    }

    @Test
    void findById_idInexistente_retornaNull() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        UsuarioResponseDTO resultado = usuarioService.findById(99L);

        assertNull(resultado);
    }

    @Test
    void update_idExistente_retornaUsuarioActualizado() {
        Usuario existente = new Usuario("Viejo", "Nombre", "viejo@test.com", "hash", "CC", "12345678");

        UsuarioUpdateRequestDTO datos = new UsuarioUpdateRequestDTO();
        datos.setNombre("Nuevo");
        datos.setApellido("Apellido");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);

        UsuarioResponseDTO resultado = usuarioService.update(1L, datos);

        assertNotNull(resultado);
        assertEquals("Nuevo", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void update_idInexistente_retornaNull() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        UsuarioResponseDTO resultado = usuarioService.update(99L, new UsuarioUpdateRequestDTO());

        assertNull(resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void delete_idExistente_llamaDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.delete(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
