package com.coroto.backend.service;

import com.coroto.backend.DTO.OrdenItemRequestDTO;
import com.coroto.backend.DTO.OrdenItemResponseDTO;
import com.coroto.backend.model.Orden;
import com.coroto.backend.model.OrdenItem;
import com.coroto.backend.model.Producto;
import com.coroto.backend.model.Usuario;
import com.coroto.backend.model.enums.EstadoPago;
import com.coroto.backend.model.enums.EstadoPedido;
import com.coroto.backend.repository.OrdenItemRepository;
import com.coroto.backend.repository.OrdenRepository;
import com.coroto.backend.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrdenItemServiceTest {

    @Mock
    private OrdenItemRepository ordenItemRepository;

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private OrdenItemService ordenItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_existenItems_retornaLista() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden orden = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE, "Calle 1", "Bogotá", usuario);
        Producto producto = new Producto();
        producto.setNombre("RTX 4090");

        OrdenItem item1 = new OrdenItem(2, BigDecimal.valueOf(1999.99), orden, producto);
        OrdenItem item2 = new OrdenItem(1, BigDecimal.valueOf(500.0), orden, producto);

        when(ordenItemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<OrdenItemResponseDTO> resultado = ordenItemService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ordenItemRepository, times(1)).findAll();
    }

    @Test
    void findById_idExistente_retornaDTO() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden orden = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE, "Calle 1", "Bogotá", usuario);
        Producto producto = new Producto();
        producto.setNombre("RTX 4090");

        OrdenItem item = new OrdenItem(2, BigDecimal.valueOf(1999.99), orden, producto);

        when(ordenItemRepository.findById(1L)).thenReturn(Optional.of(item));

        OrdenItemResponseDTO resultado = ordenItemService.findById(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.getCantidad());
    }

    @Test
    void findById_idInexistente_retornaNull() {
        when(ordenItemRepository.findById(99L)).thenReturn(Optional.empty());

        OrdenItemResponseDTO resultado = ordenItemService.findById(99L);

        assertNull(resultado);
    }

    @Test
    void save_datosValidos_retornaItemCreado() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden orden = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE, "Calle 1", "Bogotá", usuario);
        Producto producto = new Producto();
        producto.setNombre("RTX 4090");

        OrdenItemRequestDTO dto = new OrdenItemRequestDTO();
        dto.setOrdenId(1L);
        dto.setProductoId(1L);
        dto.setCantidad(2);
        dto.setPrecioUnitario(BigDecimal.valueOf(1999.99));

        OrdenItem itemGuardado = new OrdenItem(2, BigDecimal.valueOf(1999.99), orden, producto);

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ordenItemRepository.save(any(OrdenItem.class))).thenReturn(itemGuardado);

        OrdenItemResponseDTO resultado = ordenItemService.save(dto);

        assertNotNull(resultado);
        assertEquals(2, resultado.getCantidad());
        verify(ordenItemRepository, times(1)).save(any(OrdenItem.class));
    }

    @Test
    void save_ordenNoEncontrada_lanzaExcepcion() {
        OrdenItemRequestDTO dto = new OrdenItemRequestDTO();
        dto.setOrdenId(99L);
        dto.setProductoId(1L);

        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> ordenItemService.save(dto));

        assertTrue(excepcion.getMessage().contains("Pedido no encontrado"));
        verify(ordenItemRepository, never()).save(any());
    }

    @Test
    void save_productoNoEncontrado_lanzaExcepcion() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden orden = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE, "Calle 1", "Bogotá", usuario);

        OrdenItemRequestDTO dto = new OrdenItemRequestDTO();
        dto.setOrdenId(1L);
        dto.setProductoId(99L);

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> ordenItemService.save(dto));

        assertTrue(excepcion.getMessage().contains("Producto no encontrado"));
        verify(ordenItemRepository, never()).save(any());
    }

    @Test
    void update_idExistente_retornaItemActualizado() {
        Usuario usuario = new Usuario("Ana", "García", "ana@test.com", "hash", "CC", "11111111");
        Orden orden = new Orden(LocalDateTime.now(), EstadoPago.NO_PAGO, EstadoPedido.PENDIENTE, "Calle 1", "Bogotá", usuario);
        Producto producto = new Producto();
        producto.setNombre("RTX 4090");

        OrdenItem existente = new OrdenItem(1, BigDecimal.valueOf(100.0), orden, producto);

        OrdenItemRequestDTO dto = new OrdenItemRequestDTO();
        dto.setCantidad(5);
        dto.setPrecioUnitario(BigDecimal.valueOf(200.0));

        when(ordenItemRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ordenItemRepository.save(existente)).thenReturn(existente);

        OrdenItemResponseDTO resultado = ordenItemService.update(1L, dto);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCantidad());
        verify(ordenItemRepository, times(1)).save(existente);
    }

    @Test
    void update_idInexistente_retornaNull() {
        when(ordenItemRepository.findById(99L)).thenReturn(Optional.empty());

        OrdenItemResponseDTO resultado = ordenItemService.update(99L, new OrdenItemRequestDTO());

        assertNull(resultado);
        verify(ordenItemRepository, never()).save(any());
    }

    @Test
    void delete_idExistente_llamaDeleteById() {
        doNothing().when(ordenItemRepository).deleteById(1L);

        ordenItemService.delete(1L);

        verify(ordenItemRepository, times(1)).deleteById(1L);
    }
}
