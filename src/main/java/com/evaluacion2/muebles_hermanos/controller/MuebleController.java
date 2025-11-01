package com.evaluacion2.muebles_hermanos.controller;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.service.MuebleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/muebles")
public class MuebleController {
    private final MuebleService muebleService;

    public MuebleController(MuebleService muebleService) {
        this.muebleService = muebleService;
    }

    // [CREATE] POST /api/catalogo/muebles
    @PostMapping
    public ResponseEntity<Mueble> crearMueble(@RequestBody Mueble nuevoMueble) {
        try {
            Mueble creado = muebleService.crearMueble(nuevoMueble);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Manejo de errores de negocio (ej: stock negativo)
            return ResponseEntity.badRequest().build(); 
        }
    }

    // [READ] GET /api/catalogo/muebles
    @GetMapping
    public List<Mueble> listarMuebles() {
        return muebleService.listarTodos();
    }

    // [READ] GET /api/catalogo/muebles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Mueble> obtenerMueble(@PathVariable int id) {
        return muebleService.listarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // [UPDATE] PUT /api/catalogo/muebles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Mueble> actualizarMueble(@PathVariable int id, @RequestBody Mueble detallesMueble) {
        return muebleService.actualizarMueble(id, detallesMueble)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // [DESACTIVAR] PUT /api/catalogo/muebles/{id}/desactivar
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarMueble(@PathVariable int id) {
        if (muebleService.desactivarMueble(id)) {
            return ResponseEntity.ok().build(); 
        }
        return ResponseEntity.notFound().build();
    }

    // [ACTIVAR] PUT /api/catalogo/muebles/{id}/activar
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarMueble(@PathVariable int id) {
        if (muebleService.activarMueble(id)) {
            return ResponseEntity.ok().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }
}
