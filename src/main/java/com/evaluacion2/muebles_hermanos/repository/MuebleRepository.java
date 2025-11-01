package com.evaluacion2.muebles_hermanos.repository;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuebleRepository extends JpaRepository<Mueble, Integer> {

}
