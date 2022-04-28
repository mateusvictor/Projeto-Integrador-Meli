package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CartItemRequest {

    @NotEmpty(message = "productId must not be blank")
    private String productId;

    @NotNull(message = "quantity must not be blank")
    @Positive(message = "quantity must not be negative or zero")
    private int quantity;
}
