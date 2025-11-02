package com.evaluacion2.muebles_hermanos.strategy;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;


@Component
public class PrecioEstandarStrategy implements CalculoPrecioStrategy{

    @Override
    public BigDecimal calcularPrecioUnitario(Mueble mueble, List<Variante> variantes) {
        if (variantes == null || variantes.isEmpty()) {
            return mueble.getPrecioBase();
        }

        // Suma de todos los precios adicionales de las variantes aplicadas
        BigDecimal costoAdicional = variantes.stream()
            .map(Variante::getPrecioAdicional)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Precio Unitario Final = Precio Base + Costo Adicional
        return mueble.getPrecioBase().add(costoAdicional);
    }
    
}
