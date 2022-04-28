package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.InboundOrderRequest;
import br.com.meli.fresh.dto.response.InboundOrderResponse;
import br.com.meli.fresh.model.InboundOrder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InboundOrderMapper {
    private final ModelMapper modelMapper;

    public InboundOrder toDomainObject(InboundOrderRequest dto){
        return modelMapper.map(dto, InboundOrder.class);
    }

    public InboundOrderResponse toResponseObject(InboundOrder entity){
        return modelMapper.map(entity, InboundOrderResponse.class);
    }
}
