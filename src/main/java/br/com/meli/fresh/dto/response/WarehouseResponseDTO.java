package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseResponseDTO {
    private String id;
    private String name;
    private List<SectionNestedResponseDTO> sectionList;
    private WarehouseManagerNestedResponseDTO warehouseManagerId;
}
