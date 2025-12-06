package com.evaluacion2.muebles_hermanos.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "mueble")
@Data
public class Mueble {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mueble")
    private Integer idMueble;

    @Column(name = "nombre_mueble", nullable = false)
    private String nombreMueble;

    @Column(name = "tipo", nullable = false)
    private String tipo; 
    @Column(name = "precio_base", nullable = false)
    private BigDecimal precioBase;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "estado", nullable = false)
    private String estado; 

    @Column(name = "tamano", nullable = false)
    private String tamano;

    @Column(name = "material", nullable = false)
    private String material;
}
