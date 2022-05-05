package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoRequest {
    @NotBlank(message = "The title can not be emptied")
    private String title;
    @NotBlank(message = "The url can not be emptied")
    private String url;
    @NotNull(message = "The length can not be emptied")
    private Integer length;
    @NotBlank(message = "The productId can not be emptied")
    private String productId;
}
