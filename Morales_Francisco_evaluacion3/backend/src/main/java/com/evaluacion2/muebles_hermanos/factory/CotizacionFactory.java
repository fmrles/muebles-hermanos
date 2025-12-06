package com.evaluacion2.muebles_hermanos.factory;

import com.evaluacion2.muebles_hermanos.model.dto.CotizacionRequest;
import com.evaluacion2.muebles_hermanos.model.dto.ItemRequest;
import com.evaluacion2.muebles_hermanos.model.entity.*;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import com.evaluacion2.muebles_hermanos.repository.VarianteRepository;
import com.evaluacion2.muebles_hermanos.service.PrecioService;

import java.util.List;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class CotizacionFactory {
    
    private final MuebleRepository muebleRepository;
    private final VarianteRepository varianteRepository;
    private final PrecioService precioService;

    public CotizacionFactory(MuebleRepository muebleRepository, VarianteRepository varianteRepository, PrecioService precioService) {
        this.muebleRepository = muebleRepository;
        this.varianteRepository = varianteRepository;
        this.precioService = precioService;
    }

    public Cotizacion crearCotizacion(CotizacionRequest request) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setFechaCreacion(LocalDateTime.now());
        cotizacion.setEstado("Cotizado");
        
        List<CotizacionItem> items = request.getItems().stream()
                .map(itemRequest -> crearCotizacionItem(itemRequest, cotizacion))
                .toList();
        cotizacion.setItems(items);

        cotizacion.getItems().forEach(item -> item.setCotizacion(cotizacion));

        BigDecimal totalFinal = items.stream()
            .map(CotizacionItem::getPrecioItemTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        cotizacion.setTotalFinal(totalFinal);

        return cotizacion;
    }

    // Método auxiliar para construir CotizacionItem
    private CotizacionItem crearCotizacionItem(ItemRequest itemRequest, Cotizacion cotizacion) {
        
        Mueble mueble = muebleRepository.getReferenceById(itemRequest.getMuebleId());
        
        CotizacionItem item = new CotizacionItem();
        item.setMueble(mueble);
        item.setCantidad(itemRequest.getCantidad());

        BigDecimal precioTotalItem = precioService.calcularPrecioTotalItem(
            itemRequest.getMuebleId(), 
            itemRequest.getVarianteIds(), 
            itemRequest.getCantidad()
        );
        item.setPrecioItemTotal(precioTotalItem);

        // Crear y vincular ItemVariante (M:N)
        item.setVariantesAplicadas(
            itemRequest.getVarianteIds().stream()
                .map(varianteId -> crearItemVariante(item, varianteId))
                .toList()
        );
        
        return item;
    }

    // Método auxiliar para crear la entidad de enlace M:N
    private ItemVariante crearItemVariante(CotizacionItem item, Integer varianteId) {
        Variante variante = varianteRepository.getReferenceById(varianteId);
        
        ItemVariante iv = new ItemVariante();
        iv.setItem(item);
        iv.setVariante(variante);
        return iv;
    }
}
