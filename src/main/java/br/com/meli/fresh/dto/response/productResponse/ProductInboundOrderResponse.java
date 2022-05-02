package br.com.meli.fresh.dto.response.productResponse;

import br.com.meli.fresh.model.Section;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductInboundOrderResponse {
    private String id;
    private LocalDateTime orderDateTime;
    private ProductSectionResponse section;
}
