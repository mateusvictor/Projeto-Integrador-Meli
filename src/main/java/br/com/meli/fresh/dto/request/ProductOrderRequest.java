package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOrderRequest {
    private String productId;
    private int quantity;
}
