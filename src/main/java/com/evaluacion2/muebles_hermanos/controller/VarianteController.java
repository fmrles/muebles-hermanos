package com.evaluacion2.muebles_hermanos.controller;

import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import com.evaluacion2.muebles_hermanos.service.VarianteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/catalogo/variantes")
public class VarianteController {
    
    private final VarianteService varianteService;

    public VarianteController(VarianteService varianteService) {
        this.varianteService = varianteService;
    }

    @GetMapping
    public List<Variante> listarVariantes() {
        return varianteService.listarTodas();
    }
}
