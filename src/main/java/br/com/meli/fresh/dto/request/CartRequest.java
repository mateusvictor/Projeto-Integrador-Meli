package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CartRequest {

    @NotNull(message = "status must not be blank")
    private String status;

    @Valid
    @NotNull(message = "items must not be blank")
    private List<CartItemRequest> items;
}
