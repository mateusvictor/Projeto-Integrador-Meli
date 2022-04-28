package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class BatchFactory {

    public static Batch createBatch(Product product, InboundOrder inboundOrder) {
        Batch batch = new Batch();
        batch.setInitialQuantity(1);
        batch.setCurrentQuantity(10);
        batch.setManufacturingDateTime(LocalDateTime.now());
        batch.setDueDate(LocalDate.now().plusMonths(1));
        batch.setVolume(10f);
        batch.setProduct(product);
        batch.setInboundOrder(inboundOrder);
        return batch;
    }

    public static Batch createBatch(Product product, InboundOrder inboundOrder, int currentQuantity, LocalDate dueDate) {
        Batch batch = createBatch(product, inboundOrder);
        batch.setCurrentQuantity(currentQuantity);
        batch.setDueDate(dueDate);
        return  batch;
    }

}
