package br.com.meli.fresh.dto.request;

import br.com.meli.fresh.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GeneralUserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String role;
    private UserType userType;
}
