package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseNestedRequestDTO {
    @NotNull(message = "ID must not be null!")
    private String id;
}
