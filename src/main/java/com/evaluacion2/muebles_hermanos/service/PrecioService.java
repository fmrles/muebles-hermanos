package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import com.evaluacion2.muebles_hermanos.repository.VarianteRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PrecioService {

    private final MuebleRepository muebleRepository;
    private final VarianteRepository varianteRepository;

    public PrecioService(MuebleRepository muebleRepository, VarianteRepository varianteRepository) {
        this.muebleRepository = muebleRepository;
        this.varianteRepository = varianteRepository;
    }

    public BigDecimal calcularPrecioTotalItem(int muebleId, List<Integer> varianteIds, int cantidad) {
        // 1. Obteniene Precio Base del Mueble
        Mueble mueble = muebleRepository.findById(muebleId)
                .orElseThrow(() -> new IllegalArgumentException("Mueble no encontrado."));

        BigDecimal precioUnitario = mueble.getPrecioBase();
        BigDecimal costoAdicionalVariantes = BigDecimal.ZERO;
        
        // 2. Suma los Precios Adicionales de las Variantes
        if (varianteIds != null && !varianteIds.isEmpty()) {
            List<Variante> variantes = varianteRepository.findAllById(varianteIds);
            
            if (variantes.size() != varianteIds.size()) {
                throw new IllegalArgumentException("Al menos una variante no fue encontrada.");
            }

            costoAdicionalVariantes = variantes.stream()
                .map(Variante::getPrecioAdicional)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Suma de todos los precios adicionales
        } else {
             // Para esta lógica, si no se envía nada, asumo que se asigna la variante normal (ID 1).
             Variante normal = varianteRepository.findById(1)
                 .orElseThrow(() -> new IllegalStateException("Variante 'Normal' no encontrada."));
             
             costoAdicionalVariantes = normal.getPrecioAdicional(); // 0.00
        }
        
        // 3. Calcula el Precio Unitario Final
        precioUnitario = precioUnitario.add(costoAdicionalVariantes);

        // 4. Calcula el Precio Total del Item (Precio Unitario * Cantidad)
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

}
