package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.CartRequest;
import br.com.meli.fresh.dto.response.CartItemResponse;
import br.com.meli.fresh.dto.response.CartResponse;
import br.com.meli.fresh.model.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartMapper {

    private final ModelMapper modelMapper;

    public Cart toDomainObject(CartRequest dto) {
        List<CartItem> list = dto.getItems().stream().map(p -> {
            CartItem npo = new CartItem();
            Product np = new Product();
            np.setId(p.getProductId());
            npo.setQuantity(p.getQuantity());
            npo.setProduct(np);
            return npo;
        }).collect(Collectors.toList());

        Cart object = modelMapper.map(dto, Cart.class);
        object.setCartStatus(CartStatus.valueOf(dto.getStatus().toUpperCase()));

        Buyer buyer = new Buyer();
        buyer.setId(dto.getBuyerId());
        object.setItems(list);
        object.setBuyer(buyer);
        return object;
    }

    public CartResponse toResponseObject(Cart entity) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setDate(entity.getDate().toString());

        cartResponse.setItems(new ArrayList<>());

        cartResponse.setStatus(entity.getCartStatus().cartStatus);
        entity.getItems().stream().forEach(product -> {
            cartResponse.getItems().add(new CartItemResponse(product.getProduct().getId(), product.getQuantity()));
        });

        return cartResponse;
    }
}
