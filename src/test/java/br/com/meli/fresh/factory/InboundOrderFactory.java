package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.Section;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

public class InboundOrderFactory {

    public static InboundOrder createInboundOrder(Section section) {
        InboundOrder inboundOrder = new InboundOrder();
        inboundOrder.setOrderDateTime(LocalDateTime.now());
        inboundOrder.setSection(section);

        return inboundOrder;
    }
}
