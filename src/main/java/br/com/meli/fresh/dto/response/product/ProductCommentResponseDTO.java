package br.com.meli.fresh.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCommentResponseDTO {

    private String id;
    private String productId;
    private String buyerId;
    private String description;
    private Integer rating;
    private LocalDateTime commentDateTime;
}
