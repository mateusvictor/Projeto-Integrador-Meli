package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BuyerRequestDTO {
    private String name;
    private String email;
    private String password;
}
