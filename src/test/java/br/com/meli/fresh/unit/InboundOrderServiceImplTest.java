package br.com.meli.fresh.unit;

import br.com.meli.fresh.factory.InboundOrderFactory;
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

    @Test
    public void testInboundOrderCreation(){
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();
        Mockito.when(orderRepository.save(inboundOrder)).thenReturn(inboundOrder);
        inboundOrder = orderService.create(inboundOrder);
        assertNotNull(inboundOrder);
    }

    @Test
    public void testGetInboundOrder(){
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();
        Mockito.when(orderRepository.findById("order1")).thenReturn(Optional.of(inboundOrder));
        inboundOrder = orderService.getById("order1");
        assertNotNull(inboundOrder);
    }

    @Test
    public void testUpdateInboundOrder(){
        // First creates an inbound order
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();
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
        InboundOrder invalidInboundOrder = InboundOrderFactory.getInstanceWithInvalidProductType();
        assertThrows(InvalidSectionTypeException.class, () -> {
            orderService.create(invalidInboundOrder);
        });
    }

    @Test
    public void testInsufficientAvailableSpaceException(){
        InboundOrder invalidInboundOrder = InboundOrderFactory.getInstanceWithInvalidVolume();
        assertThrows(InsufficientAvailableSpaceException.class, () -> {
            orderService.create(invalidInboundOrder);
        });
    }
}
