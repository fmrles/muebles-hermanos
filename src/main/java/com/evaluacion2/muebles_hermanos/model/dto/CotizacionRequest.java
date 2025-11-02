package com.evaluacion2.muebles_hermanos.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class CotizacionRequest {
    private List<ItemRequest> items;
}
