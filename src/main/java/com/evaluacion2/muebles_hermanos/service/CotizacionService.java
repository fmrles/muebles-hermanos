package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.dto.CotizacionRequest;
import com.evaluacion2.muebles_hermanos.model.entity.*;
import com.evaluacion2.muebles_hermanos.repository.*;
import com.evaluacion2.muebles_hermanos.factory.CotizacionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CotizacionService {
    
    private final CotizacionRepository cotizacionRepository;
    private final CotizacionItemRepository itemRepository;
    private final MuebleRepository muebleRepository;
    private final CotizacionFactory cotizacionFactory;

    public CotizacionService(CotizacionRepository cotizacionRepository, CotizacionItemRepository itemRepository, MuebleRepository muebleRepository, CotizacionFactory cotizacionFactory) {
        this.cotizacionRepository = cotizacionRepository;
        this.itemRepository = itemRepository;
        this.muebleRepository = muebleRepository;
        this.cotizacionFactory = cotizacionFactory;
    }

    @Transactional
    public Cotizacion crearCotizacion(CotizacionRequest request) {
        Cotizacion cotizacion = cotizacionFactory.crearCotizacion(request);
        return cotizacionRepository.save(cotizacion);
    }

    
    //Confirmo una cotización como venta, decrementando stock.
    @Transactional(rollbackFor = {IllegalArgumentException.class}) // La transacción se revierte si hay error de stock
    public Cotizacion confirmarVenta(Integer cotizacionId) {
        Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId)
            .orElseThrow(() -> new IllegalArgumentException("Cotización no encontrada."));

        if (cotizacion.getEstado().equals("Confirmado")) {
            throw new IllegalArgumentException("La cotización ya ha sido confirmada como venta.");
        }

        List<CotizacionItem> items = itemRepository.findByCotizacion_IdCotizacion(cotizacionId);

        // 1. Valido y Decremento el Stock
        for (CotizacionItem item : items) {
            Mueble mueble = item.getMueble();
            int cantidadSolicitada = item.getCantidad();
            
            if (mueble.getStock() < cantidadSolicitada) {
                // Mensaje de error (stock insuficiente) y se hace un rollback.
                throw new IllegalArgumentException("stock insuficiente para el mueble: " + mueble.getNombreMueble());
            }

            // Decremento el Stock
            int nuevoStock = mueble.getStock() - cantidadSolicitada;
            mueble.setStock(nuevoStock);

            if(nuevoStock == 0) {
                mueble.setEstado("Inactivo");
            }

            muebleRepository.save(mueble);
            
        }

        // 2. Marco como Confirmado (Venta)
        cotizacion.setEstado("Confirmado");
        cotizacion.setFechaConfirmacion(LocalDateTime.now());
        return cotizacionRepository.save(cotizacion);
    }

    public List<Cotizacion> listarTodas() {
        return cotizacionRepository.findAll();
    }
}
