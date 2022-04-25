package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.ProductRequest;
import br.com.meli.fresh.dto.request.SellerRequest;
import br.com.meli.fresh.dto.response.SellerResponse;
import br.com.meli.fresh.dto.response.productResponse.ProductResponse;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.Seller;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public Product toDomainObject(ProductRequest dto) {
        return modelMapper.map(dto, Product.class);
    }

    public ProductResponse toResponseObject(Product entity) {
        return modelMapper.map(entity, ProductResponse.class);
    }
}
