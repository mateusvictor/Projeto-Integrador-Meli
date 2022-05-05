package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.response.SaleResponse;
import br.com.meli.fresh.model.Batch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SaleMapper {

    public SaleResponse toResponseObject(Batch entity) {
        SaleResponse response = SaleResponse.builder()
                .productId(entity.getProduct().getId())
                .name(entity.getProduct().getName())
                .dueDate(entity.getDueDate().toString())
                .price(entity.getProduct().getPrice())
                .quantity(entity.getCurrentQuantity())
                .batchId(entity.getId())
                .build();
        return response;
    }
}
