package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponse {
    private String productId;
    private String name;
    private LocalDate dueDate;
    private BigDecimal price;
    private int quantity;
    private String batchId;

}
