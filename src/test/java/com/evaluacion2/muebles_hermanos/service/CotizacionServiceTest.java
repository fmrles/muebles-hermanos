package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.entity.Cotizacion;
import com.evaluacion2.muebles_hermanos.model.entity.CotizacionItem;
import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.repository.CotizacionItemRepository;
import com.evaluacion2.muebles_hermanos.repository.CotizacionRepository;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CotizacionServiceTest {

    @Mock
    private CotizacionRepository cotizacionRepository;
    @Mock
    private CotizacionItemRepository itemRepository;
    @Mock
    private MuebleRepository muebleRepository;
    
    // Ignoramos PrecioService y CotizacionFactory en 'confirmarVenta'
    // porque no se usan en ese método específico.

    @InjectMocks
    private CotizacionService cotizacionService;

    // Test (Stock/Venta - Venta exitosa)
    @Test
    void testConfirmarVenta_StockSuficiente_DecrementaStockYConfirma() {
        Integer cotizacionId = 1;
        
        // 1. Mocks de Muebles e Items
        Mueble muebleEnStock = new Mueble();
        muebleEnStock.setIdMueble(100);
        muebleEnStock.setNombreMueble("Silla Cara");
        muebleEnStock.setStock(10); // Stock inicial
        muebleEnStock.setEstado("Activo");

        CotizacionItem item = new CotizacionItem();
        item.setCantidad(3); // Cantidad solicitada
        item.setMueble(muebleEnStock);

        List<CotizacionItem> items = List.of(item);

        // 2. Mock de Cotización
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(cotizacionId);
        cotizacion.setEstado("Cotizado"); // Estado inicial

        // 3. Configuración de Mocks
        when(cotizacionRepository.findById(cotizacionId)).thenReturn(Optional.of(cotizacion));
        when(itemRepository.findByCotizacion_IdCotizacion(cotizacionId)).thenReturn(items);
        when(cotizacionRepository.save(any(Cotizacion.class))).thenReturn(cotizacion); // Devuelve la cotización al guardar

        // 4. Ejecutar el Servicio
        Cotizacion cotizacionConfirmada = cotizacionService.confirmarVenta(cotizacionId);

        // 5. Verificar (¡AQUÍ ESTÁ LA CORRECCIÓN!)
        
        // 5a. Verificar que el MUEBLE fue guardado con el stock correcto
        ArgumentCaptor<Mueble> muebleCaptor = ArgumentCaptor.forClass(Mueble.class);
        // Verificamos que se llamó 1 vez a muebleRepository.save()
        verify(muebleRepository, times(1)).save(muebleCaptor.capture()); 
        
        Mueble muebleGuardado = muebleCaptor.getValue();
        assertEquals(7, muebleGuardado.getStock()); // 10 - 3 = 7
        assertEquals("Activo", muebleGuardado.getEstado());

        // 5b. Verificar que la COTIZACIÓN fue guardada con el estado correcto
        // Verificamos que se llamó 1 vez a cotizacionRepository.save()
        verify(cotizacionRepository, times(1)).save(any(Cotizacion.class));
        assertEquals("Confirmado", cotizacionConfirmada.getEstado());
    }
    
    // Test (Stock/Venta - Stock insuficiente)
    @Test
    void testConfirmarVenta_StockInsuficiente_LanzaExcepcion() {
        Integer cotizacionId = 1;

        // 1. Mocks
        Mueble muebleSinStock = new Mueble();
        muebleSinStock.setIdMueble(101);
        muebleSinStock.setNombreMueble("Mesa Grande");
        muebleSinStock.setStock(5); // Stock inicial

        CotizacionItem item = new CotizacionItem();
        item.setCantidad(6); // Se piden 6, pero solo hay 5
        item.setMueble(muebleSinStock);

        List<CotizacionItem> items = List.of(item);
        
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(cotizacionId);
        cotizacion.setEstado("Cotizado");

        // 2. Configuración de Mocks
        when(cotizacionRepository.findById(cotizacionId)).thenReturn(Optional.of(cotizacion));
        when(itemRepository.findByCotizacion_IdCotizacion(cotizacionId)).thenReturn(items);

        // 3. Ejecutar y Verificar (Assert)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cotizacionService.confirmarVenta(cotizacionId);
        });

        assertEquals("stock insuficiente para el mueble: Mesa Grande", exception.getMessage());
        
        // Verificamos que NUNCA se guardó nada (Rollback)
        verify(muebleRepository, never()).save(any(Mueble.class));
        verify(cotizacionRepository, never()).save(any(Cotizacion.class));
    }
    
    @Test
    void testConfirmarVenta_StockSuficiente_PoneInactivoSiStockLlegaACero() {
        Integer cotizacionId = 1;
        
        Mueble muebleEnStock = new Mueble();
        muebleEnStock.setStock(2); // Stock inicial
        muebleEnStock.setEstado("Activo");

        CotizacionItem item = new CotizacionItem();
        item.setCantidad(2); // Cantidad solicitada (deja en 0)
        item.setMueble(muebleEnStock);
        
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setEstado("Cotizado");

        when(cotizacionRepository.findById(cotizacionId)).thenReturn(Optional.of(cotizacion));
        when(itemRepository.findByCotizacion_IdCotizacion(cotizacionId)).thenReturn(List.of(item));

        cotizacionService.confirmarVenta(cotizacionId);

        ArgumentCaptor<Mueble> muebleCaptor = ArgumentCaptor.forClass(Mueble.class);
        verify(muebleRepository).save(muebleCaptor.capture());
        
        Mueble muebleGuardado = muebleCaptor.getValue();
        assertEquals(0, muebleGuardado.getStock()); // Stock 0
        assertEquals("Inactivo", muebleGuardado.getEstado()); // Estado debe cambiar a Inactivo
    }
}