package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class InboundOrderRequest {
    private LocalDateTime orderDateTime;

    @NotBlank(message = "sectionId must not be blank")
    private String sectionId;

    @NotEmpty(message = "batchStock must not be empty")
    private List<@Valid BatchRequest> batchStock;
}
