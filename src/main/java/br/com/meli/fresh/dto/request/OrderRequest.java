package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private String buyerId;
    private OrderStatusRequest orderStatus;
    private List<ProductOrderRequest> products;
}
