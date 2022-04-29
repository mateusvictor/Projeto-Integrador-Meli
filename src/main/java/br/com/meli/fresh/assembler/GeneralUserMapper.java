package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.BuyerRequestDTO;
import br.com.meli.fresh.dto.request.GeneralUserRequestDTO;
import br.com.meli.fresh.dto.response.BuyerResponseDTO;
import br.com.meli.fresh.dto.response.GeneralUserResponseDTO;
import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.GeneralUser;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GeneralUserMapper {
    private final ModelMapper modelMapper;

    public GeneralUser toDomainObject(GeneralUserRequestDTO dto) {
        return modelMapper.map(dto, GeneralUser.class);
    }

    public GeneralUserResponseDTO toResponseObject(GeneralUser entity) {
        return modelMapper.map(entity, GeneralUserResponseDTO.class);
    }
}
