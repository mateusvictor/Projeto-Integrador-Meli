package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponse {
    private String id;
    private String name;
    private String category;
    private Float minTemperature;
    private Float maxTemperature;
    private Float weight;
}
