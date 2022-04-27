package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchRequest {
    @NotBlank(message = "productId must not be blank")
    private String productId;

    @NotNull(message = "currentTemperature must not be null")
    private Float currentTemperature;

    @NotNull(message = "initialQuantity must not be null")
    private Integer initialQuantity;

    @NotNull(message = "currentQuantity must not be null")
    private Integer currentQuantity;

    private LocalDateTime manufacturingDateTime;

    @NotNull(message = "dueDate must not be null")
    private LocalDate dueDate;

    @NotNull(message = "volume must not be null")
    private Float volume;
}
