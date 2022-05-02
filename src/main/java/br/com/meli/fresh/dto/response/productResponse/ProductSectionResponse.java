package br.com.meli.fresh.dto.response.productResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSectionResponse {
    private String id;
    private String productType;
    private Float actualVolume;
    private Float maxVolume;
    private ProductWarehouseResponse warehouse;
}
