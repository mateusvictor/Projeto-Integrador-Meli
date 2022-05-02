package br.com.meli.fresh.dto.response.productResponse;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private String id;
    private String name;
    private String category;
    private Float minTemperature;
    private Float maxTemperature;
    private Float weight;
    private BigDecimal price;
    private List<ProductBatchResponse> batchList;
    private boolean isActive;
}
