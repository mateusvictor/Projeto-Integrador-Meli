package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseRequestDTO {
    @NotNull(message = "name must not be null")
    @NotBlank(message = "name must not be blank")
    private String name;
    @NotEmpty(message = "sectionList must not be empty")
    private List<@Valid SectionRequestDTO> sectionList;
    @NotNull(message = "warehouseManagerId must not be null")
    @NotBlank(message = "warehouseManagerId must not be blank")
    private String warehouseManagerId;
}
