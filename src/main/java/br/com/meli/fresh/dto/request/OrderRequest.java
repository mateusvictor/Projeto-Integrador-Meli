package br.com.meli.fresh.dto.request;

import br.com.meli.fresh.model.Buyer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private String buyerId;
    private OrderStatusRequest orderStatus;
    private List<OrderProductRequest> product;


}
