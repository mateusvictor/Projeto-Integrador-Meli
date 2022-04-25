package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.BuyerRequestDTO;
import br.com.meli.fresh.dto.response.BuyerResponseDTO;
import br.com.meli.fresh.model.Buyer;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class BuyerMapper {

    private final ModelMapper modelMapper;

    public Buyer toDomainObject(BuyerRequestDTO dto) {
        return modelMapper.map(dto, Buyer.class);
    }

    public BuyerResponseDTO toResponseObject(Buyer entity) {
        return modelMapper.map(entity, BuyerResponseDTO.class);
    }


}
