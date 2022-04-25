package br.com.meli.fresh.dto.response.productResponse;

import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProductBatchResponse {

    private String id;
    private Float currentTemperature;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private LocalDateTime manufacturingDateTime;
    private LocalDate dueDate;
    private Float volume;
    private InboundOrder inboundOrder;

}
