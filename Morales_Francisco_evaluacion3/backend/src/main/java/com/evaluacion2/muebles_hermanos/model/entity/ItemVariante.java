package com.evaluacion2.muebles_hermanos.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "item_variante")
@IdClass(ItemVarianteId.class)
@Data
public class ItemVariante {
    
    // Clave Compuesta 
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonIgnoreProperties({"variantesAplicadas", "hibernateLazyInitializer", "handler"})
    private CotizacionItem item; // Referencia al ítem de la cotización

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Variante variante; // Referencia a la variante aplicada
}
