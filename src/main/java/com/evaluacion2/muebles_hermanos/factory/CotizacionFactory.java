package com.evaluacion2.muebles_hermanos.factory;

import com.evaluacion2.muebles_hermanos.model.dto.CotizacionRequest;
import com.evaluacion2.muebles_hermanos.model.dto.ItemRequest;
import com.evaluacion2.muebles_hermanos.model.entity.*;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import com.evaluacion2.muebles_hermanos.repository.VarianteRepository;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class CotizacionFactory {
    
    private final MuebleRepository muebleRepository;
    private final VarianteRepository varianteRepository;

    public CotizacionFactory(MuebleRepository muebleRepository, VarianteRepository varianteRepository) {
        this.muebleRepository = muebleRepository;
        this.varianteRepository = varianteRepository;
    }

    public Cotizacion crearCotizacion(CotizacionRequest request, BigDecimal totalFinal) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setFechaCreacion(LocalDateTime.now());
        cotizacion.setEstado("Cotizado");
        cotizacion.setTotalFinal(totalFinal);
        
        // Crea y vincula los CotizacionItem y sus ItemVariante
        cotizacion.setItems(
            request.getItems().stream()
                .map(itemRequest -> crearCotizacionItem(itemRequest, cotizacion))
                .toList()
        );

        // Asegura la bidireccionalidad (JPA requiere esto)
        cotizacion.getItems().forEach(item -> item.setCotizacion(cotizacion));

        return cotizacion;
    }

    // Método auxiliar para construir CotizacionItem
    private CotizacionItem crearCotizacionItem(ItemRequest itemRequest, Cotizacion cotizacion) {
        
        Mueble mueble = muebleRepository.getReferenceById(itemRequest.getMuebleId());
        
        CotizacionItem item = new CotizacionItem();
        item.setMueble(mueble);
        item.setCantidad(itemRequest.getCantidad());
        item.setPrecioItemTotal(BigDecimal.ZERO); // PLACEHOLDER, el servicio lo actualiza con el precio real

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
