package br.com.meli.fresh.dto.request.productRequest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {

    @NotNull(groups = OnCreate.class, message = "Name cant not be null.")
    private String name;

    @NotNull(groups = OnCreate.class, message = "Category can not be null.")
    private String category;

    @NotNull(groups = OnCreate.class, message = "Minimum temperature can not be null.")
    private Float minTemperature;

    @NotNull(groups = OnCreate.class, message = "Maximum temperature can not be null.")
    private Float maxTemperature;

    @NotNull(groups = OnCreate.class, message = "Weight can not be null.")
    private Float weight;

    @NotNull(groups = OnCreate.class, message = "Price can not be null.")
    private BigDecimal price;

}
