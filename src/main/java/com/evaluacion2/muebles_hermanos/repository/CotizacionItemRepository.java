package com.evaluacion2.muebles_hermanos.repository;

import com.evaluacion2.muebles_hermanos.model.entity.CotizacionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CotizacionItemRepository extends JpaRepository<CotizacionItem, Integer> {
    List<CotizacionItem> findByCotizacionId(Integer cotizacionId);
}
