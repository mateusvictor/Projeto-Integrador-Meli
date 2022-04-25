package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}
