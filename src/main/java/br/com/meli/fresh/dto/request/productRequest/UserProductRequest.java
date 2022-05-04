package br.com.meli.fresh.dto.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProductRequest {
    private String id;
    private String name;
    private String email;
    private String password;
    private Set<Integer> roles;
}
