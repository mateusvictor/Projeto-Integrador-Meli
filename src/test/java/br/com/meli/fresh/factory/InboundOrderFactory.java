package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class InboundOrderFactory {
    public static InboundOrder getValidInstance(){
        Warehouse warehouse = new Warehouse("warehouse1", "SP - WAREHOUSE", null, null);
        Section section = new Section("section1", "fresco", 0F, 30F, null, null);
        InboundOrder inboundOrder = new InboundOrder("order1", null, null, section);

        Product product1 = new Product("product1", "Bolacha Trakinas", "fresco", 5F, 30F, 0.25F, null);
        Product product2 = new Product("product2", "Batata Doce", "fresco", 10F, 30F, 1F, null);

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
        Warehouse warehouse = new Warehouse("warehouse1", "SP - WAREHOUSE", null, null);
        Section section = new Section("section1", "fresco", 0F, 30F, null, null);
        InboundOrder inboundOrder = new InboundOrder("order1", null, null, section);

        Product product1 = new Product("product1", "Pizza de Calabresa", "congelado", -10F, 15F, 0.75F, null);

        List<Batch> batchList = Arrays.asList(
                new Batch("batch1", 10F, 8, 8, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 10F, product1, inboundOrder)
        );

        inboundOrder.setBatchList(batchList);
        return inboundOrder;
    }

    public static InboundOrder getInstanceWithInvalidVolume(){
        // Returns an instance with batch list with more volume than the section available volume
        // Section available volume: 20.0
        // Batch total volume: 30
        Warehouse warehouse = new Warehouse("warehouse1", "SP - WAREHOUSE", null, null);
        Section section = new Section("section1", "fresco", 0F, 20F, null, null);
        InboundOrder inboundOrder = new InboundOrder("order1", null, null, section);

        Product product1 = new Product("product1", "Bolacha Trakinas", "fresco", 5F, 30F, 0.25F, null);
        Product product2 = new Product("product2", "Batata Doce", "fresco", 10F, 30F, 1F, null);

        List<Batch> batchList = Arrays.asList(
                new Batch("batch1", 10F, 8, 8, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 10F, product1, inboundOrder),
                new Batch("batch2", 10F, 5, 5, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-12-18"), 20F, product2, inboundOrder)
        );

        inboundOrder.setBatchList(batchList);
        return inboundOrder;
    }
}
