package br.com.meli.fresh.dto.response.productResponse;

import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.model.WarehouseManager;

import java.util.List;

public class ProductSectionResponse {
    private String id;
    private String productType;
    private Float actualVolume;
    private Float maxVolume;
    private ProductWarehouseResponse warehouse;

    private WarehouseManager warehouseManager;
}
