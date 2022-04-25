package br.com.meli.fresh.dto.response;

import br.com.meli.fresh.model.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseManagerResponseDTO {
    private String name;
    private String email;
    private Warehouse warehouse;
}
