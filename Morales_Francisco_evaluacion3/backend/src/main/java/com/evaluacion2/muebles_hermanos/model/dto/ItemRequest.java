package com.evaluacion2.muebles_hermanos.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class ItemRequest {
    private Integer muebleId;
    private Integer cantidad;
    private List<Integer> varianteIds;
}
