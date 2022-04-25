package br.com.meli.fresh.dto.response.productResponse;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;

import java.time.LocalDateTime;
import java.util.List;

public class ProductInboundOrderResponse {
    private String id;
    private LocalDateTime orderDateTime;
    private Section section;
}
