package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SectionRequestDTO {
    private String productType;
    private Float actualVolume;
    private Float maxVolume;
}
