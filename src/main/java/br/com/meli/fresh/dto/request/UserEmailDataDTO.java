package br.com.meli.fresh.dto.request;

import br.com.meli.fresh.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class UserEmailDataDTO {
    private String ownerRef;
    private String name;
    private String emailTo;

}
