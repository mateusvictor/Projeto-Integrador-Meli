package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.response.BatchProductResponse;
import br.com.meli.fresh.model.Batch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BatchDueDateMapper {

        public BatchProductResponse toResponseObject(Batch entity){
                BatchProductResponse response = BatchProductResponse.builder()
                        .id(entity.getId())
                        .productId(entity.getProduct().getId())
                        .dueDate(entity.getDueDate())
                        .productType(entity.getInboundOrder().getSection().getProductType())
                        .quantity(entity.getCurrentQuantity())
                        .build();
                return response;
        }
}
