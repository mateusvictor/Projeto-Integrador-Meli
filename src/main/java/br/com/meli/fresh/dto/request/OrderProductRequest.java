package br.com.meli.fresh.dto.request;

import br.com.meli.fresh.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductRequest {
    private String productId;
    private int quantity;
}
