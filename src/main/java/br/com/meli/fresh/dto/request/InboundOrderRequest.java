package br.com.meli.fresh.dto.request;

import br.com.meli.fresh.model.Batch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class InboundOrderRequest {
    private LocalDateTime orderDateTime;

    @NotBlank(message = "sectionId must not be blank")
    private String sectionId;

    @NotEmpty(message = "batchStock must be empty")
    private List<BatchRequest> batchStock;
}