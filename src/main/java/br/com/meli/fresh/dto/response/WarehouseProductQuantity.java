package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseProductQuantity {
    private String warehouseId;
    private Integer totalQuantity;
}
