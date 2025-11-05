package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional; // Asegúrate de importar este

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // <- IMPORTANTE: Revierte los cambios en la BDD después de cada test
class MuebleServiceTest {

    @Autowired
    private MuebleService muebleService;

    @Autowired
    private MuebleRepository muebleRepository;

    private Mueble muebleDePrueba;

    @BeforeEach
    void setUp() {
        muebleRepository.deleteAll(); // Limpia la BDD de H2

        muebleDePrueba = new Mueble();
        muebleDePrueba.setNombreMueble("Silla de Prueba");
        muebleDePrueba.setTipo("Silla");
        muebleDePrueba.setPrecioBase(new BigDecimal("10000"));
        muebleDePrueba.setStock(50);
        muebleDePrueba.setMaterial("Madera");
        muebleDePrueba.setTamano("Mediano");
        // El estado 'Activo' debe asignarlo el servicio
    }

    // Test (CREATE)
    @Test
    void testCrearMueble_AsignaEstadoActivoYVerificaPersistencia() {
        Mueble muebleCreado = muebleService.crearMueble(muebleDePrueba);

        assertNotNull(muebleCreado.getIdMueble());
        assertEquals("Activo", muebleCreado.getEstado());
        assertEquals(1, muebleRepository.count());
    }

    // Test (CREATE) - Validación de Stock Negativo
    @Test
    void testCrearMueble_StockNegativo_LanzaExcepcion() {
        muebleDePrueba.setStock(-10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            muebleService.crearMueble(muebleDePrueba);
        });
        
        assertEquals("El stock inicial no puede ser negativo.", exception.getMessage());
    }

    // Test (READ)
    @Test
    void testListarTodos_VerificaPersistencia() {
        muebleService.crearMueble(muebleDePrueba);
        
        List<Mueble> muebles = muebleService.listarTodos();
        
        assertFalse(muebles.isEmpty());
        assertEquals(1, muebles.size());
        assertEquals("Silla de Prueba", muebles.get(0).getNombreMueble());
    }

    // Test (UPDATE)
    @Test
    void testActualizarMueble_ActualizaDatos() {
        Mueble muebleCreado = muebleService.crearMueble(muebleDePrueba);
        Integer idCreado = muebleCreado.getIdMueble();

        Mueble detallesUpdate = new Mueble();
        detallesUpdate.setNombreMueble("Silla Actualizada");
        detallesUpdate.setStock(99);
        detallesUpdate.setPrecioBase(new BigDecimal("15000"));
        detallesUpdate.setMaterial("Metal"); // Atributos no actualizables por el servicio
        detallesUpdate.setTamano("Grande"); // Atributos no actualizables por el servicio


        Optional<Mueble> muebleActualizadoOpt = muebleService.actualizarMueble(idCreado, detallesUpdate);

        assertTrue(muebleActualizadoOpt.isPresent());
        Mueble muebleActualizado = muebleActualizadoOpt.get();

        assertEquals(idCreado, muebleActualizado.getIdMueble());
        assertEquals("Silla Actualizada", muebleActualizado.getNombreMueble());
        assertEquals(99, muebleActualizado.getStock());
        assertEquals(0, new BigDecimal("15000").compareTo(muebleActualizado.getPrecioBase()));
    }

    // Test (DESACTIVAR)
    @Test
    void testDesactivarMueble_CambiaEstadoAInactivo() {
        Mueble muebleCreado = muebleService.crearMueble(muebleDePrueba);
        assertEquals("Activo", muebleCreado.getEstado());

        boolean resultado = muebleService.desactivarMueble(muebleCreado.getIdMueble());
        
        assertTrue(resultado);
        Mueble muebleDesactivado = muebleRepository.findById(muebleCreado.getIdMueble()).get();
        assertEquals("Inactivo", muebleDesactivado.getEstado());
    }
    
    // Test (ACTIVAR)
    @Test
    void testActivarMueble_CambiaEstadoAActivo() {
        Mueble muebleCreado = muebleService.crearMueble(muebleDePrueba);
        muebleService.desactivarMueble(muebleCreado.getIdMueble()); // Primero lo desactivamos

        boolean resultado = muebleService.activarMueble(muebleCreado.getIdMueble());
        
        assertTrue(resultado);
        Mueble muebleActivado = muebleRepository.findById(muebleCreado.getIdMueble()).get();
        assertEquals("Activo", muebleActivado.getEstado());
    }
}