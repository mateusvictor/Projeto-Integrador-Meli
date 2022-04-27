package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.*;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IInboundOrderRepository;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import br.com.meli.fresh.services.exception.InsufficientAvailableSpaceException;
import br.com.meli.fresh.services.exception.InvalidSectionTypeException;
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceImplTest {
    @Mock
    private static IInboundOrderRepository orderRepository;
    @Mock
    private static IBatchRepository batchRepository;
    @Mock
    private static ISectionRepository sectionRepository;

    @InjectMocks
    private InboundOrderServiceImpl orderService;

    private InboundOrder getValidInstance(){
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

    private InboundOrder getInstanceWithInvalidProductType(){
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

    private InboundOrder getInstanceWithInvalidVolume(){
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


    @Test
    public void testInboundOrderCreation(){
        InboundOrder inboundOrder = this.getValidInstance();
        Mockito.when(orderRepository.save(inboundOrder)).thenReturn(inboundOrder);
        inboundOrder = orderService.create(inboundOrder);
        assertNotNull(inboundOrder);
    }

    @Test
    public void testGetInboundOrder(){
        InboundOrder inboundOrder = this.getValidInstance();
        Mockito.when(orderRepository.findById("order1")).thenReturn(Optional.of(inboundOrder));
        inboundOrder = orderService.getById("order1");
        assertNotNull(inboundOrder);
    }

    @Test
    public void testUpdateInboundOrder(){
        // First creates an inbound order
        InboundOrder inboundOrder = this.getValidInstance();
        Mockito.when(orderRepository.save(inboundOrder)).thenReturn(inboundOrder);
        inboundOrder = orderService.create(inboundOrder);
        Double oldTotalVolume = inboundOrder.calculateBatchesTotalVolume();

        // Mocks the method called in the update method
        Mockito.when(orderRepository.findById(inboundOrder.getId())).thenReturn(Optional.of(inboundOrder));

        // Updates the volume of one batch
        InboundOrder inboundOrderUpdated = inboundOrder;

        List<Batch> batchListUpdated = inboundOrderUpdated.getBatchList();
        batchListUpdated.get(0).setVolume(5F);
        inboundOrderUpdated.setBatchList(batchListUpdated);

        inboundOrderUpdated = orderService.update(inboundOrder.getId(), inboundOrderUpdated);
        System.out.println(inboundOrderUpdated.calculateBatchesTotalVolume());

        assertEquals(inboundOrder.getId(), inboundOrderUpdated.getId());
        assertNotEquals(oldTotalVolume, inboundOrder.calculateBatchesTotalVolume());
    }

    @Test
    public void testInboundOrderNotFoundException(){
        Mockito.when(orderRepository.findById("invalidId")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.getById("invalidId");
        });
    }

    @Test
    public void testInvalidSectionTypeException(){
        InboundOrder invalidInboundOrder = this.getInstanceWithInvalidProductType();
        assertThrows(InvalidSectionTypeException.class, () -> {
            orderService.create(invalidInboundOrder);
        });
    }

    @Test
    public void TestInsufficientAvailableSpaceException(){
        InboundOrder invalidInboundOrder = this.getInstanceWithInvalidVolume();
        assertThrows(InsufficientAvailableSpaceException.class, () -> {
            orderService.create(invalidInboundOrder);
        });
    }
}
