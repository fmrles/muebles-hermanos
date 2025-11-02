package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.dto.CotizacionRequest;
import com.evaluacion2.muebles_hermanos.model.dto.ItemRequest;
import com.evaluacion2.muebles_hermanos.model.entity.*;
import com.evaluacion2.muebles_hermanos.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CotizacionService {
    
    private final CotizacionRepository cotizacionRepository;
    private final CotizacionItemRepository itemRepository;
    private final ItemVarianteRepository itemVarianteRepository;
    private final MuebleRepository muebleRepository;
    private final VarianteRepository varianteRepository;
    private final PrecioService precioService; 

    public CotizacionService(CotizacionRepository cotizacionRepository, CotizacionItemRepository itemRepository, 
                             ItemVarianteRepository itemVarianteRepository, MuebleRepository muebleRepository,
                             VarianteRepository varianteRepository, PrecioService precioService) {
        this.cotizacionRepository = cotizacionRepository;
        this.itemRepository = itemRepository;
        this.itemVarianteRepository = itemVarianteRepository;
        this.muebleRepository = muebleRepository;
        this.varianteRepository = varianteRepository;
        this.precioService = precioService;
    }

    @Transactional
    public Cotizacion crearCotizacion(CotizacionRequest request) {
        BigDecimal totalFinal = BigDecimal.ZERO;
        List<CotizacionItem> itemsGuardados = new ArrayList<>();
        
        // 1. Crear la cabecera de la Cotización
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setFechaCreacion(LocalDateTime.now());
        cotizacion.setEstado("Cotizado");
        cotizacion.setTotalFinal(BigDecimal.ZERO); // Se actualiza al final
        cotizacion = cotizacionRepository.save(cotizacion);

        // 2. Procesar cada Item
        for (ItemRequest itemRequest : request.getItems()) {
            // Usa el PrecioService para obtener el precio total del ítem
            BigDecimal precioTotalItem = precioService.calcularPrecioTotalItem(
                itemRequest.getMuebleId(), 
                itemRequest.getVarianteIds(), 
                itemRequest.getCantidad()
            );
            
            // 3. Crear CotizacionItem
            CotizacionItem item = new CotizacionItem();
            item.setCotizacion(cotizacion);
            item.setMueble(muebleRepository.getReferenceById(itemRequest.getMuebleId()));
            item.setCantidad(itemRequest.getCantidad());
            item.setPrecioItemTotal(precioTotalItem);
            item = itemRepository.save(item);
            
            // 4. Crear los registros de ItemVariante (M:N)
            for (Integer varianteId : itemRequest.getVarianteIds()) {
                ItemVariante iv = new ItemVariante();
                iv.setItem(item);
                iv.setVariante(varianteRepository.getReferenceById(varianteId));
                itemVarianteRepository.save(iv);
            }
            
            totalFinal = totalFinal.add(precioTotalItem);
            itemsGuardados.add(item);
        }
        
        // 5. Actualizar y devolver el Total Final
        cotizacion.setTotalFinal(totalFinal);
        cotizacion.setItems(itemsGuardados);
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

        List<CotizacionItem> items = itemRepository.findByCotizacionId(cotizacionId);

        // 1. Valido y Decremento el Stock
        for (CotizacionItem item : items) {
            Mueble mueble = item.getMueble();
            int cantidadSolicitada = item.getCantidad();
            
            if (mueble.getStock() < cantidadSolicitada) {
                // Mensaje de error (stock insuficiente) y se hace un rollback.
                throw new IllegalArgumentException("stock insuficiente para el mueble: " + mueble.getNombreMueble());
            }

            // Decremento el Stock
            mueble.setStock(mueble.getStock() - cantidadSolicitada);
            muebleRepository.save(mueble);
        }

        // 2. Marco como Confirmado (Venta)
        cotizacion.setEstado("Confirmado");
        cotizacion.setFechaConfirmacion(LocalDateTime.now());
        return cotizacionRepository.save(cotizacion);
    }
}
