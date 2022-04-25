package br.com.meli.fresh.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerResponse {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
}
