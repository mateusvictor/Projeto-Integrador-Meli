package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.InboundOrderRequest;
import br.com.meli.fresh.dto.request.TrackingRecordRequest;
import br.com.meli.fresh.dto.response.InboundOrderResponse;
import br.com.meli.fresh.dto.response.TrackingRecordResponse;
import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.TrackingRecord;
import br.com.meli.fresh.services.impl.TrackingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TrackingRecordMapper {
    private final ModelMapper modelMapper;

    public TrackingRecord toDomainObject(TrackingRecordRequest dto){
        return modelMapper.map(dto, TrackingRecord.class);
    }

    public TrackingRecordResponse toResponseObject(TrackingRecord entity){
        return modelMapper.map(entity, TrackingRecordResponse.class);
    }

    public List<TrackingRecordResponse> toResponseObject(List<TrackingRecord> entityList){
        return entityList.stream().map(this::toResponseObject).collect(Collectors.toList());
    }
}
