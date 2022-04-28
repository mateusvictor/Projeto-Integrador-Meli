package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SectionResponse {
    private String id;
    private String productType;
    private Float actualVolume;
    private Float maxVolume;
    private WarehouseResponse warehouse;
}
