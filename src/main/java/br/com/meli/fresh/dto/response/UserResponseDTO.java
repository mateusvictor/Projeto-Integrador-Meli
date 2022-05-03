package br.com.meli.fresh.dto.response;

import br.com.meli.fresh.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private Set<Role> roles;
}
