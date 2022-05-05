package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoResponse {
    private String id;
    private String title;
    private String url;
    private Integer length;
    private boolean approved;
}
