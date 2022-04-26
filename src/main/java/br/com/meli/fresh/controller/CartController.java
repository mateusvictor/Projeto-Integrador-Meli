package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.CartMapper;
import br.com.meli.fresh.dto.request.CartRequest;
import br.com.meli.fresh.dto.response.OrderTotalPriceResponse;
import br.com.meli.fresh.model.Cart;
import br.com.meli.fresh.model.CartStatus;
import br.com.meli.fresh.model.exception.InvalidEnumCartStatusException;
import br.com.meli.fresh.services.impl.CartServiceImpl;
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
public class CartController {
    private CartServiceImpl cartService;
    private final CartMapper mapper;

    @PostMapping
    public ResponseEntity<OrderTotalPriceResponse> creteCart(@Valid @RequestBody CartRequest request, UriComponentsBuilder uriBuilder){

        if(!CartStatus.isEnumValid(request.getStatus().toUpperCase())){
            throw new InvalidEnumCartStatusException("Status not valid.");
        }
        OrderTotalPriceResponse orderTotalPriceResponse = new OrderTotalPriceResponse();
        Cart cart = cartService.create(mapper.toDomainObject(request));

        // validacoes dos dados de entrada
        // validacao da quantidade de podutos
        // validacoes personalizadas
        // criar update e get

        // Add product price later
        Double sum = cart.getItems().stream().reduce(0.0, (subtotal, element) -> subtotal + element.getQuantity(), Double::sum);
        orderTotalPriceResponse.setTotalPrice(sum);

        URI uri = uriBuilder
                .path("api/v1/fresh-products/orders/{id}")
                .buildAndExpand(cart.getId())
                .toUri();

        return ResponseEntity.created(uri).body(orderTotalPriceResponse);
    }

}
