package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerRequestDTO {
    private String name;
    private String email;
    private String password;
    private String role;
}