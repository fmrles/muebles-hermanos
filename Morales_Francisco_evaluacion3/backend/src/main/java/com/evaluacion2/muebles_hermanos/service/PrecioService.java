package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import com.evaluacion2.muebles_hermanos.repository.VarianteRepository;
import com.evaluacion2.muebles_hermanos.strategy.CalculoPrecioStrategy;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PrecioService {

    private final MuebleRepository muebleRepository;
    private final VarianteRepository varianteRepository;
    private final CalculoPrecioStrategy precioStrategy;

    public PrecioService(MuebleRepository muebleRepository, VarianteRepository varianteRepository, CalculoPrecioStrategy precioStrategy) {
        this.muebleRepository = muebleRepository;
        this.varianteRepository = varianteRepository;
        this.precioStrategy = precioStrategy;
    }

    public BigDecimal calcularPrecioTotalItem(Integer muebleId, List<Integer> varianteIds, int cantidad) {
        // 1. Obteniene Precio Base del Mueble
        Mueble mueble = muebleRepository.findById(muebleId)
                .orElseThrow(() -> new IllegalArgumentException("Mueble no encontrado."));

        List<Variante> variantes = varianteRepository.findAllById(varianteIds);

        if (variantes.size() != varianteIds.size()) {
            throw new IllegalArgumentException("Al menos una variante no fue encontrada.");
        }

        //ahora uso la estrategia para calcular el precio unitario
        BigDecimal precioUnitario = precioStrategy.calcularPrecioUnitario(mueble, variantes);

        //Calclulo el precio total del item (precio unitario * cantidad)
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

}
