package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SectionNestedResponseDTO {
    private String id;
    private String productType;
    private Float actualVolume;
    private Float maxVolume;
}
