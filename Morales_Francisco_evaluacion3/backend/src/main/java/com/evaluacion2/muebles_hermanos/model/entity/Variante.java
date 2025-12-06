package com.evaluacion2.muebles_hermanos.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "variante")
@Data
public class Variante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante")
    private Integer idVariante;

    @Column(name = "nombre_variante", nullable = false)
    private String nombreVariante;

    @Column(name = "precio_adicional", nullable = false)
    private BigDecimal precioAdicional;

    private String descripcion;
}
