package br.com.meli.fresh.dto.response.productResponse;

import br.com.meli.fresh.model.User;

public class ProductSectionResponse {
    private String id;
    private String productType;
    private Float actualVolume;
    private Float maxVolume;
    private ProductWarehouseResponse warehouse;

    private User warehouseManager;
}
