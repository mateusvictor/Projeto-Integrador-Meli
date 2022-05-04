package br.com.meli.fresh.dto.response;

import br.com.meli.fresh.model.OrderStatus;
import br.com.meli.fresh.model.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrackingRecordResponse {
    private String id;
    private LocalDateTime date;
    private String location;
    private String detailMessage;
    private String orderStatus;
    private String orderId;
}
