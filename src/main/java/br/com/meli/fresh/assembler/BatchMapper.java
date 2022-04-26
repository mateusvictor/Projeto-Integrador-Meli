package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.BatchRequest;
import br.com.meli.fresh.dto.request.InboundOrderRequest;
import br.com.meli.fresh.dto.response.BatchResponse;
import br.com.meli.fresh.dto.response.InboundOrderResponse;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.InboundOrder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BatchMapper {
    private final ModelMapper modelMapper;

    public Batch toDomainObject(BatchRequest dto){
        Batch batch = modelMapper.map(dto, Batch.class);
        batch.setId(null);
        return batch;
    }

    public BatchResponse toResponseObject(Batch entity){
        return modelMapper.map(entity, BatchResponse.class);
    }
}
