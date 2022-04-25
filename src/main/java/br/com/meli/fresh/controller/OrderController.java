package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.PurchaseOrderMapper;
import br.com.meli.fresh.dto.request.OrderRequest;
import br.com.meli.fresh.dto.response.OrderTotalPriceResponse;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.services.impl.PurchaseOrderServiceImpl;
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
    private PurchaseOrderServiceImpl purchaseOrderService;
    private final PurchaseOrderMapper mapper;



    @PostMapping
    public ResponseEntity<OrderTotalPriceResponse> createOrder(@Valid @RequestBody OrderRequest request, UriComponentsBuilder uriBuilder){

        OrderTotalPriceResponse orderTotalPriceResponse = new OrderTotalPriceResponse();
        PurchaseOrder purchaseOrder = purchaseOrderService.create(mapper.toDomainObject(request));
        // Add product price later
        Double sum = purchaseOrder.getProducts().stream().reduce(0.0,
                                                (subtotal, element) -> subtotal + element.getQuantity(), Double::sum);
        orderTotalPriceResponse.setTotalPrice(sum);

        URI uri = uriBuilder
                .path("{id}")
                .buildAndExpand()
                .toUri();

        return ResponseEntity.created(uri).body(orderTotalPriceResponse);
    }

}
