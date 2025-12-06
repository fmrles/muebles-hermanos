package com.evaluacion2.muebles_hermanos.controller;

import com.evaluacion2.muebles_hermanos.model.dto.CotizacionRequest;
import com.evaluacion2.muebles_hermanos.model.entity.Cotizacion;
import com.evaluacion2.muebles_hermanos.service.CotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    public CotizacionController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    /**
     * POST /cotizaciones : Crea una nueva cotización.
     */
    @PostMapping
    public ResponseEntity<Cotizacion> crearCotizacion(@RequestBody CotizacionRequest request) {
        try {
            Cotizacion nuevaCotizacion = cotizacionService.crearCotizacion(request);
            return new ResponseEntity<>(nuevaCotizacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /cotizaciones/{id}/confirmar : Confirma la cotización como venta y decrementa stock.
     */
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Cotizacion> confirmarVenta(@PathVariable Integer id) {
        try {
            Cotizacion ventaConfirmada = cotizacionService.confirmarVenta(id);
            return ResponseEntity.ok(ventaConfirmada);
        } catch (IllegalArgumentException e) {
            // Manejo el error 404 (Cotización no encontrada) como el error 400 (Stock insuficiente)
            if (e.getMessage().contains("stock insuficiente")) {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 
            }
            return ResponseEntity.notFound().build(); // 404 
        }
    }

    //Get /cotizaciones : Listar todas las cotizaciones
    @GetMapping
    public List<Cotizacion> listarCotizaciones() {
        return cotizacionService.listarTodas();
    }
}
