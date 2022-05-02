package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductQuantityResponse {
    private String productId;
    private List<WarehouseProductQuantity> warehouses;
}
