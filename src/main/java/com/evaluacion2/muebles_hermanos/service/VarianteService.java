package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import com.evaluacion2.muebles_hermanos.repository.VarianteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VarianteService {
    private final VarianteRepository varianteRepository;

    public VarianteService(VarianteRepository varianteRepository) {
        this.varianteRepository = varianteRepository;
    }

    public List<Variante> listarTodas() {
        return varianteRepository.findAll();
    }
}
