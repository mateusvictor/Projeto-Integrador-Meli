package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.CartMapper;
import br.com.meli.fresh.dto.request.CartRequest;
import br.com.meli.fresh.dto.response.CartResponse;
import br.com.meli.fresh.dto.response.OrderTotalPriceResponse;
import br.com.meli.fresh.model.Cart;
import br.com.meli.fresh.model.CartStatus;
import br.com.meli.fresh.model.exception.InvalidEnumCartStatusException;
import br.com.meli.fresh.services.ICartService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("api/v1/fresh-products/orders")
@AllArgsConstructor
public class CartController {
    private final ICartService cartService;
    private final CartMapper mapper;

    @ApiOperation(value = "This endpoint creates a cart shopping to buy products from this storage app.")
    @PostMapping
    public ResponseEntity<OrderTotalPriceResponse> creteCart(@Valid @RequestBody CartRequest request, UriComponentsBuilder uriBuilder){

        if(!CartStatus.isEnumValid(request.getStatus().toUpperCase())){
            throw new InvalidEnumCartStatusException("Status not valid.");
        }
        OrderTotalPriceResponse orderTotalPriceResponse = new OrderTotalPriceResponse();
        Cart cart = cartService.create(mapper.toDomainObject(request));

        BigDecimal sum = cart.getItems().stream().reduce(BigDecimal.ZERO, (subtotal, element) -> BigDecimal.valueOf(element.getQuantity()).multiply(element.getProduct().getPrice()), BigDecimal::add);
        orderTotalPriceResponse.setTotalPrice(sum);

        URI uri = uriBuilder
                .path("api/v1/fresh-products/orders/{id}")
                .buildAndExpand(cart.getId())
                .toUri();

        return ResponseEntity.created(uri).body(orderTotalPriceResponse);
    }

    @ApiOperation(value = "This endpoint provides a possibility to update the cart shopping.")
    @PutMapping("{id}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toResponseObject(cartService.update(id)));
    }

    @ApiOperation(value = "This endpoint provides a possibility to check one cart shopping.")
    @GetMapping("{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toResponseObject(cartService.getById(id)));
    }

}
