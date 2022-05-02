package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SectionRequestDTO {
    private String id;
    @NotBlank(message = "productType must not be blank")
    private String productType;
    @NotNull(message = "actualVolume must not be null")
    @Min(value = 0, message = "actualVolume min value is 0")
    private Float actualVolume;
    @NotNull(message = "actualVolume must not be null")
    @Min(value = 1, message = "actualVolume min value is 1")
    private Float maxVolume;
}
