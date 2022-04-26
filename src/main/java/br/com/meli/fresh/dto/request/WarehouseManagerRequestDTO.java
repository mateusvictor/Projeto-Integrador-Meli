package br.com.meli.fresh.dto.request;

import br.com.meli.fresh.model.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseManagerRequestDTO {
    private String name;
    private String email;
    private String password;
    private String warehouse;
}
