package com.coroto.backend.service;

import com.coroto.backend.model.Producto;
import com.coroto.backend.model.enums.CategoriaProducto;
import com.coroto.backend.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_existenProductos_retornaLista() {
        Producto p1 = new Producto();
        p1.setNombre("Laptop Gaming");
        Producto p2 = new Producto();
        p2.setNombre("Monitor 4K");

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void findById_idExistente_retornaProducto() {
        Producto producto = new Producto();
        producto.setNombre("Laptop Gaming");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Laptop Gaming", resultado.getNombre());
    }

    @Test
    void findById_idInexistente_retornaNull() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = productoService.findById(99L);

        assertNull(resultado);
    }

    @Test
    void save_productoValido_retornaProductoGuardado() {
        Producto producto = new Producto();
        producto.setNombre("RTX 4090");
        producto.setCategoria(CategoriaProducto.TARJETAS_VIDEO);
        producto.setPrecio(1999.99);
        producto.setCantidad(5);

        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        assertEquals("RTX 4090", resultado.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void update_idExistente_retornaProductoActualizado() {
        Producto existente = new Producto();
        existente.setNombre("Viejo nombre");
        existente.setPrecio(100.0);

        Producto datos = new Producto();
        datos.setNombre("Nuevo nombre");
        datos.setCategoria(CategoriaProducto.LAPTOPS);
        datos.setDescripcion("Descripción actualizada");
        datos.setPrecio(200.0);
        datos.setCantidad(10);
        datos.setImageUrl("http://img.com/nuevo.png");
        datos.setActivo(true);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(existente)).thenReturn(existente);

        Producto resultado = productoService.update(1L, datos);

        assertNotNull(resultado);
        assertEquals("Nuevo nombre", resultado.getNombre());
        assertEquals(200.0, resultado.getPrecio());
        verify(productoRepository, times(1)).save(existente);
    }

    @Test
    void update_idInexistente_retornaNull() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = productoService.update(99L, new Producto());

        assertNull(resultado);
        verify(productoRepository, never()).save(any());
    }

    @Test
    void delete_idExistente_llamaDeleteById() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.delete(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
