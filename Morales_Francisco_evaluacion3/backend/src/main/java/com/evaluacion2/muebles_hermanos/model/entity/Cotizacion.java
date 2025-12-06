package com.evaluacion2.muebles_hermanos.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cotizacion")
@Data
public class Cotizacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    private Integer idCotizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    @Column(name = "estado", nullable = false)
    private String estado; 

    @Column(name = "total_final", nullable = false)
    private BigDecimal totalFinal;

    // Relación 1:N con los ítems de cotización
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"cotizacion", "hibernateLazyInitializer", "handler"})
    private List<CotizacionItem> items;
}
