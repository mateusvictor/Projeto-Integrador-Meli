package br.com.meli.fresh.dto.response;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
