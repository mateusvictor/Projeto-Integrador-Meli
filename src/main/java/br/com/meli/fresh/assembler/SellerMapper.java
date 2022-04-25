package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.SellerRequest;
import br.com.meli.fresh.dto.response.SellerResponse;
import br.com.meli.fresh.model.Seller;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SellerMapper {

    private final ModelMapper modelMapper;

    public Seller toDomainObject(SellerRequest dto) {
        return modelMapper.map(dto, Seller.class);
    }

    public SellerResponse toResponseObject(Seller entity) {
        return modelMapper.map(entity, SellerResponse.class);
    }

}
