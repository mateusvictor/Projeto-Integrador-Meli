package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.BuyerRequestDTO;
import br.com.meli.fresh.dto.request.WarehouseManagerRequestDTO;
import br.com.meli.fresh.dto.response.BuyerResponseDTO;
import br.com.meli.fresh.dto.response.WarehouseManagerResponseDTO;
import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.WarehouseManager;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WarehouseManagerMapper {

    private final ModelMapper modelMapper;

    public WarehouseManager toDomainObject(WarehouseManagerRequestDTO dto) {
        return modelMapper.map(dto, WarehouseManager.class);
    }

    public WarehouseManagerResponseDTO toResponseObject(WarehouseManager entity) {
        return modelMapper.map(entity, WarehouseManagerResponseDTO.class);
    }
}
