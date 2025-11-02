package com.evaluacion2.muebles_hermanos.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class ItemVarianteId implements Serializable {
    
    private Integer item; // Mapea a CotizacionItem
    private Integer variante; // Mapea a Variante

    // Requeridos por JPA
    public ItemVarianteId() {}

    // Implementación de equals() y hashCode() es obligatoria
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVarianteId that = (ItemVarianteId) o;
        return Objects.equals(item, that.item) && Objects.equals(variante, that.variante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, variante);
    }
    
}
