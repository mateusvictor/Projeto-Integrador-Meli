package br.com.meli.fresh.dto.response;

import br.com.meli.fresh.model.UserType;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GeneralUserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private UserType userType;
}
