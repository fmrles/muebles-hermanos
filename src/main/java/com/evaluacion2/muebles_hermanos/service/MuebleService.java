package com.evaluacion2.muebles_hermanos.service;

import com.evaluacion2.muebles_hermanos.model.entity.Mueble;
import com.evaluacion2.muebles_hermanos.repository.MuebleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MuebleService {
    
    private final MuebleRepository muebleRepository;

    public MuebleService(MuebleRepository muebleRepository) {
        this.muebleRepository = muebleRepository;
    }

    // CRUD: CREATE (Crear un nuevo mueble)
    @Transactional
    public Mueble crearMueble(Mueble mueble) {
        // Lógica de validación inicial (ej: stock no puede ser negativo)
        if (mueble.getStock() < 0) {
            throw new IllegalArgumentException("El stock inicial no puede ser negativo.");
        }
        if (mueble.getEstado() == null || mueble.getEstado().isEmpty()) {
             mueble.setEstado("Activo"); // Estado por defecto
        }
        return muebleRepository.save(mueble);
    }

    // CRUD: READ (Listar todos los muebles)
    public List<Mueble> listarTodos() {
        return muebleRepository.findAll();
    }
    
    // CRUD: READ (Listar por ID)
    public Optional<Mueble> listarPorId(Integer id) {
        return muebleRepository.findById(id);
    }

    // CRUD: UPDATE 
    @Transactional
    public Optional<Mueble> actualizarMueble(Integer id, Mueble detallesMueble) {
        return muebleRepository.findById(id)
            .map(muebleExistente -> {
                muebleExistente.setNombreMueble(detallesMueble.getNombreMueble());
                muebleExistente.setPrecioBase(detallesMueble.getPrecioBase());
                muebleExistente.setStock(detallesMueble.getStock());
                muebleExistente.setTamano(detallesMueble.getTamano());
                muebleExistente.setMaterial(detallesMueble.getMaterial());
                return muebleRepository.save(muebleExistente);
            });
    }

    // CRUD: DESACTIVAR
    @Transactional
    public boolean desactivarMueble(Integer id) {
        return muebleRepository.findById(id)
            .map(mueble -> {
                mueble.setEstado("Inactivo");
                muebleRepository.save(mueble);
                return true;
            }).orElse(false);
    }
}
