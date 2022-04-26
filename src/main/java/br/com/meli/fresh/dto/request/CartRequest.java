package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartRequest {
    private String buyerId;
    private String status;
    private List<CartItemRequest> items;
}
