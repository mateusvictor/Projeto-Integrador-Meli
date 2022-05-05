package br.com.meli.fresh.assembler;

import br.com.meli.fresh.dto.request.VideoRequest;
import br.com.meli.fresh.dto.response.VideoResponse;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.Video;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VideoMapper {

    public Video toDomainObject(VideoRequest request){
        Product product = new Product();
        product.setId(request.getProductId());
        return  Video.builder()
                .id(null)
                .title(request.getTitle())
                .length(request.getLength())
                .url(request.getUrl())
                .product(product)
                .approved(false)
                .build();

    }

    public VideoResponse toResponseObject(Video entity){
        return VideoResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .url(entity.getUrl())
                .length(entity.getLength())
                .approved(entity.isApproved())
                .build();
    }
}
