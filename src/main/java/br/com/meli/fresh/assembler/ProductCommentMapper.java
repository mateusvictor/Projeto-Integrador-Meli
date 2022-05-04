package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.productRequest.ProductCommentRequestDTO;
import br.com.meli.fresh.dto.response.product.ProductCommentResponseDTO;
import br.com.meli.fresh.model.ProductComment;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductCommentMapper {
    private final ModelMapper modelMapper;

    public ProductComment toDomainObject(ProductCommentRequestDTO dto) {
        return modelMapper.map(dto, ProductComment.class);
    }

    public ProductCommentResponseDTO toResponseObject(ProductComment entity) {
        return modelMapper.map(entity, ProductCommentResponseDTO.class);
    }
}
