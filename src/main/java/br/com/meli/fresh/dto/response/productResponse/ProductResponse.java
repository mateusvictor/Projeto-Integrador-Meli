package br.com.meli.fresh.dto.response.productResponse;

import br.com.meli.fresh.model.Batch;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private String id;
    private String name;
    private String category;
    private Float minTemperature;
    private Float maxTemperature;
    private Float weight;
    private List<Batch> batchList;

}
