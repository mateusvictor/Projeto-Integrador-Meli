package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseManagerRequestDTO {
    private String name;
    private String email;
    private String password;
    private @Valid WarehouseNestedRequestDTO warehouse;
}
