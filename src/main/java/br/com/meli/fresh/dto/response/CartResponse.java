package br.com.meli.fresh.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDateTime;

@Getter
@Setter
public class CartResponse {
    private LocalDateTime date;
    private String status;
    private List<CartItemResponse> items;

}
