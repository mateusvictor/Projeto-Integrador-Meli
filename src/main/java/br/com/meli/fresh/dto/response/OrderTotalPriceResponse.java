package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderTotalPriceResponse {
    private BigDecimal totalPrice;
}