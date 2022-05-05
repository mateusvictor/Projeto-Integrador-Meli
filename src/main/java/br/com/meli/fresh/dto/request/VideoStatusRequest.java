package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoStatusRequest {
    @NotNull
//    @Pattern(regexp = "^true$|^false$", message = "allowed input: true or false")
    private Boolean status;
}
