package br.com.meli.fresh.dto.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductCommentRequestDTO {

//    @NotBlank(message = "productId must not be blank")
//    private String productId;
//    @NotBlank(message = "buyerId must not be blank")
//    private String buyerId;
    @Size(min=20, max=500, message = "Description size must be between 20 and 500 characters.")
    private String description;
    @Min(value = 1, message = "rating min value is 1")
    @Max(value = 5, message = "rating max value is 5")
    private Integer rating;
}
