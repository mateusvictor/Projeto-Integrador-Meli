package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private String warehouse;
    private String warehouseManager;
    private Integer totalSections;
    private Integer totalBatches;
    private Integer totalQuantityProducts;
    private Integer totalFreshProducts;
    private Double percentageOfFreshProducts;
    private Integer totalRefrigeratedProducts;
    private Double percentageOfRefrigeratedProducts;
    private Integer totalFrozenProducts;
    private Double percentageOfFrozenProducts;
    private Integer totalNextToExpiredProducts;
}
