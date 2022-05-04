package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.response.BatchProductResponse;
import br.com.meli.fresh.model.Batch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SaleMapper {

    public BatchProductResponse toResponseObject(Batch entity) {
        BatchProductResponse response = BatchProductResponse.builder()
                .productId(entity.getProduct().getId())
                .dueDate(entity.getDueDate())
                .productId(entity.getProduct().getPrice().toString())
                .build();
        return response;
    }
}
