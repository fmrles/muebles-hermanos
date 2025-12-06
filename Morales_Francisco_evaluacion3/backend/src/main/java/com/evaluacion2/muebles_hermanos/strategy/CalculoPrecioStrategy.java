package com.evaluacion2.muebles_hermanos.strategy;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import java.math.BigDecimal;
import java.util.List;

public interface CalculoPrecioStrategy {
    
    BigDecimal calcularPrecioUnitario(Mueble mueble, List<Variante> variantes);
}
