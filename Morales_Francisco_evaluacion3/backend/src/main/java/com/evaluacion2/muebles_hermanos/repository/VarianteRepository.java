package com.evaluacion2.muebles_hermanos.repository;

import com.evaluacion2.muebles_hermanos.model.entity.Variante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarianteRepository  extends JpaRepository<Variante, Integer> {
    
}
