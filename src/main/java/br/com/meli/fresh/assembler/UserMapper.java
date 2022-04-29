package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.UserRequestDTO;
import br.com.meli.fresh.dto.response.UserResponseDTO;
import br.com.meli.fresh.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toDomainObject(UserRequestDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserResponseDTO toResponseObject(User entity) {
        return modelMapper.map(entity, UserResponseDTO.class);
    }
}
