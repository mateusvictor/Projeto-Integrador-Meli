package br.com.meli.fresh.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    private String name;
    private String category;
    private Float minTemperature;
    private Float maxTemperature;
    private Float weight;

}
