package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import com.evaluacion2.muebles_hermanos.repository.VarianteRepository;
import com.evaluacion2.muebles_hermanos.strategy.CalculoPrecioStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrecioServiceTest {

    @Mock
    private MuebleRepository muebleRepository;

    @Mock
    private VarianteRepository varianteRepository;

    @Mock
    private CalculoPrecioStrategy precioStrategy;

    @InjectMocks
    private PrecioService precioService;

    @Test
    void testCalcularPrecioTotalItem_CalculoCorrecto() {
        // 1. Definir Datos de Simulación
        Integer muebleId = 1;
        Integer cantidad = 2;
        List<Integer> varianteIds = List.of(10, 20);

        Mueble muebleMock = new Mueble();
        muebleMock.setIdMueble(muebleId);
        muebleMock.setPrecioBase(new BigDecimal("100"));

        Variante variante1Mock = new Variante();
        variante1Mock.setIdVariante(10);
        variante1Mock.setPrecioAdicional(new BigDecimal("50"));
        
        Variante variante2Mock = new Variante();
        variante2Mock.setIdVariante(20);
        variante2Mock.setPrecioAdicional(new BigDecimal("30"));

        List<Variante> variantesMock = List.of(variante1Mock, variante2Mock);
        
        // Simulación del precio unitario (PrecioBase + Adicionales) = 100 + 50 + 30 = 180
        BigDecimal precioUnitarioSimulado = new BigDecimal("180");

        // 2. Configurar Mocks (Simulaciones)
        when(muebleRepository.findById(muebleId)).thenReturn(Optional.of(muebleMock));
        when(varianteRepository.findAllById(varianteIds)).thenReturn(variantesMock);
        
        // Simulamos que la estrategia calcula 180
        when(precioStrategy.calcularPrecioUnitario(muebleMock, variantesMock)).thenReturn(precioUnitarioSimulado);

        // 3. Ejecutar el Servicio
        BigDecimal precioTotal = precioService.calcularPrecioTotalItem(muebleId, varianteIds, cantidad);

        // 4. Verificar (Assert)
        // Precio Total = Precio Unitario (180) * Cantidad (2) = 360
        assertEquals(0, new BigDecimal("360").compareTo(precioTotal));
        
        // Verificamos que los mocks fueron llamados
        verify(muebleRepository).findById(muebleId);
        verify(varianteRepository).findAllById(varianteIds);
        verify(precioStrategy).calcularPrecioUnitario(muebleMock, variantesMock);
    }
    
    @Test
    void testCalcularPrecioTotalItem_VarianteNoEncontrada_LanzaExcepcion() {
        Integer muebleId = 1;
        Integer cantidad = 1;
        List<Integer> varianteIds = List.of(10, 99); // 99 no existe

        Mueble muebleMock = new Mueble();
        muebleMock.setIdMueble(muebleId);

        Variante variante1Mock = new Variante();
        variante1Mock.setIdVariante(10);
        
        // El repo solo devuelve 1 de las 2 variantes solicitadas
        List<Variante> variantesParcialesMock = List.of(variante1Mock);

        when(muebleRepository.findById(muebleId)).thenReturn(Optional.of(muebleMock));
        when(varianteRepository.findAllById(varianteIds)).thenReturn(variantesParcialesMock);
        
        // Ejecutar y Verificar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            precioService.calcularPrecioTotalItem(muebleId, varianteIds, cantidad);
        });

        assertEquals("Al menos una variante no fue encontrada.", exception.getMessage());
    }
}