package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.CartRequest;
import br.com.meli.fresh.model.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
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
        object.setCartStatus(CartStatus.valueOf(dto.getStatus()));

        Buyer buyer = new Buyer();
        buyer.setId(dto.getBuyerId());
        object.setItems(list);
        object.setBuyer(buyer);
        return object;
    }

//    public SellerResponse toResponseObject(Seller entity) {
//        return modelMapper.map(entity, SellerResponse.class);
//    }
}
