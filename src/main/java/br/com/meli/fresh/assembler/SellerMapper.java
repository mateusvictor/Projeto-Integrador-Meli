package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.SellerRequestDTO;
import br.com.meli.fresh.dto.response.SellerResponseDTO;
import br.com.meli.fresh.model.Seller;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SellerMapper {

    private final ModelMapper modelMapper;

    public Seller toDomainObject(SellerRequestDTO dto) {
        return modelMapper.map(dto, Seller.class);
    }

    public SellerResponseDTO toResponseObject(Seller entity) {
        return modelMapper.map(entity, SellerResponseDTO.class);
    }
}