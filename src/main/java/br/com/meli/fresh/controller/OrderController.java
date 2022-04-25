package br.com.meli.fresh.controller;

import br.com.meli.fresh.dto.request.OrderRequest;
import br.com.meli.fresh.dto.response.OrderTotalPriceResponse;
import br.com.meli.fresh.model.OrderStatus;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.services.impl.OrderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("api/v1/fresh-products/orders")
@AllArgsConstructor
public class OrderController {
    private OrderServiceImpl orderService;



    @PostMapping
    public ResponseEntity<OrderTotalPriceResponse> createOrder(@Valid @RequestBody OrderRequest request, UriComponentsBuilder uriBuilder){
        PurchaseOrder order = new PurchaseOrder();
        OrderStatus status = OrderStatus.valueOf(request.getOrderStatus());
        orderService.createOrder(status, );


        URI uri = uriBuilder
                .path("{id}")
                .buildAndExpand()
                .toUri();
    }

}
