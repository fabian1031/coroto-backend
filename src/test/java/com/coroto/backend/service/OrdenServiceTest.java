package com.coroto.backend.service;

import com.coroto.backend.DTO.OrdenRequestDTO;
import com.coroto.backend.DTO.OrdenResponseDTO;
import com.coroto.backend.model.Orden;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.model.enums.EstadoPago;
import com.coroto.backend.model.enums.EstadoPedido;
import com.coroto.backend.repository.OrdenRepository;
import com.coroto.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OrdenService ordenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_existenOrdenes_retornaLista() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden o1 = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE, "Calle 1", "Bogotá", usuario);
        Orden o2 = new Orden(LocalDateTime.now(), EstadoPago.APROBADO, EstadoPedido.ENVIADA, "Calle 2", "Medellín", usuario);

        when(ordenRepository.findAll()).thenReturn(List.of(o1, o2));

        List<OrdenResponseDTO> resultado = ordenService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ordenRepository, times(1)).findAll();
    }

    @Test
    void findById_idExistente_retornaOrden() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden orden = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE, "Calle 1", "Bogotá", usuario);

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));

        OrdenResponseDTO resultado = ordenService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Bogotá", resultado.getCiudadEnvio());
    }

    @Test
    void findById_idInexistente_retornaNull() {
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        OrdenResponseDTO resultado = ordenService.findById(99L);

        assertNull(resultado);
    }

    @Test
    void save_datosValidos_retornaOrdenCreada() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");

        OrdenRequestDTO dto = new OrdenRequestDTO();
        dto.setUsuarioId(1L);
        dto.setFechaPedido(LocalDateTime.now());
        dto.setEstado(EstadoPedido.PENDIENTE);
        dto.setEstadoPago(EstadoPago.NO_PAGO);
        dto.setDireccionEnvio("Calle 123");
        dto.setCiudadEnvio("Bogotá");

        Orden ordenGuardada = new Orden(
                dto.getFechaPedido(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE,
                "Calle 123", "Bogotá", usuario
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenGuardada);

        OrdenResponseDTO resultado = ordenService.save(dto);

        assertNotNull(resultado);
        assertEquals("Bogotá", resultado.getCiudadEnvio());
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }

    @Test
    void save_usuarioNoEncontrado_lanzaExcepcion() {
        OrdenRequestDTO dto = new OrdenRequestDTO();
        dto.setUsuarioId(99L);

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> ordenService.save(dto));

        assertTrue(excepcion.getMessage().contains("Usuario no encontrado"));
        verify(ordenRepository, never()).save(any());
    }

    @Test
    void update_idExistente_retornaOrdenActualizada() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden existente = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE,
                "Vieja dirección", "Cali", usuario);

        OrdenRequestDTO dto = new OrdenRequestDTO();
        dto.setUsuarioId(1L);
        dto.setFechaPedido(LocalDateTime.now());
        dto.setEstado(EstadoPedido.PROCESANDO);
        dto.setEstadoPago(EstadoPago.EN_PROCESO);
        dto.setDireccionEnvio("Nueva dirección");
        dto.setCiudadEnvio("Bogotá");

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ordenRepository.save(existente)).thenReturn(existente);

        OrdenResponseDTO resultado = ordenService.update(1L, dto);

        assertNotNull(resultado);
        assertEquals("Bogotá", resultado.getCiudadEnvio());
        verify(ordenRepository, times(1)).save(existente);
    }

    @Test
    void update_idInexistente_retornaNull() {
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        OrdenResponseDTO resultado = ordenService.update(99L, new OrdenRequestDTO());

        assertNull(resultado);
        verify(ordenRepository, never()).save(any());
    }

    @Test
    void delete_idExistente_llamaDeleteById() {
        doNothing().when(ordenRepository).deleteById(1L);

        ordenService.delete(1L);

        verify(ordenRepository, times(1)).deleteById(1L);
    }
}
