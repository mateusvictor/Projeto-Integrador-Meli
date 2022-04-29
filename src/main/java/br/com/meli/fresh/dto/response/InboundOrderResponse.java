package br.com.meli.fresh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InboundOrderResponse {
    private String id;
    private LocalDateTime orderDateTime;
    private List<BatchResponse> batchList;
    private SectionResponse section;
}
