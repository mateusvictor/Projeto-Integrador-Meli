package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.WarehouseRequestDTO;
import br.com.meli.fresh.dto.response.WarehouseResponseDTO;
import br.com.meli.fresh.model.Warehouse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WarehouseMapper {

    private final ModelMapper modelMapper;

    public Warehouse toDomainObject(WarehouseRequestDTO dto) {
        return modelMapper.map(dto, Warehouse.class);
    }

    public WarehouseResponseDTO toResponseObject(Warehouse entity) {
        return modelMapper.map(entity, WarehouseResponseDTO.class);
    }


}
