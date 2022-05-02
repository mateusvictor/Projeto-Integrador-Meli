package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class InboundOrderFactory {
    public static InboundOrder getValidInstance(){
        Warehouse warehouse = WarehouseFactory.getWarehouse();
        Section section = SectionFactory.getFreshSection();
        section.setWarehouse(warehouse);
        InboundOrder inboundOrder = new InboundOrder("order1", null, null, section);

        Product product1 = ProductFactory.getFreshProductA();
        Product product2 = ProductFactory.getFreshProductB();

        List<Batch> batchList = Arrays.asList(
                new Batch("batch1", 10F, 8, 8, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 10F, product1, inboundOrder),
                new Batch("batch2", 10F, 5, 5, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-12-18"), 10F, product2, inboundOrder)
        );

        inboundOrder.setBatchList(batchList);
        return inboundOrder;
    }

    public static InboundOrder getInstanceWithInvalidProductType(){
        // Returns an instance with a product that doesn't match the section product type
        // Section product type: fresco
        // Product type: congelado
        Warehouse warehouse = WarehouseFactory.getWarehouse();
        Section section = SectionFactory.getFreshSection();
        section.setWarehouse(warehouse);
        InboundOrder inboundOrder = new InboundOrder("order1", null, null, section);

        Product product1 = ProductFactory.getFrozenProductA();

        List<Batch> batchList = Arrays.asList(
                new Batch("batch1", 10F, 8, 8, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 10F, product1, inboundOrder)
        );

        inboundOrder.setBatchList(batchList);
        return inboundOrder;
    }

    public static InboundOrder getInstanceWithInvalidVolume(){
        // Returns an instance with batch list with more volume than the section available volume
        // Section available volume: 30.0
        // Batch total volume: 40
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();

        Product product1 = ProductFactory.getFreshProductA();
        Product product2 = ProductFactory.getFreshProductB();

        List<Batch> batchList = Arrays.asList(
                new Batch("batch1", 10F, 8, 8, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 15F, product1, inboundOrder),
                new Batch("batch2", 10F, 5, 5, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-12-18"), 25F, product2, inboundOrder)
        );

        inboundOrder.setBatchList(batchList);
        return inboundOrder;
    }
}
