package br.com.meli.fresh.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
public class BatchProductResponse {
    private String id;
    private String productId;
    private String productType;
    private LocalDate dueDate;
    private int quantity;

}
