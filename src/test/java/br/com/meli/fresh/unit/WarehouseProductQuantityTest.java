package br.com.meli.fresh.unit;

import br.com.meli.fresh.dto.response.ProductQuantityResponse;
import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.factory.SectionFactory;
import br.com.meli.fresh.factory.UserFactory;
import br.com.meli.fresh.factory.WarehouseFactory;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
import br.com.meli.fresh.services.impl.WarehouseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class WarehouseProductQuantityTest {
    @Mock
    private IWarehouseRepository repository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IProductRepository productRepository;
    @Mock
    private static UserAuthenticatedService authService;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Test
    public void testGetProductQuantityByWarehouse(){
        Product product = ProductFactory.getFreshProductA();
        Warehouse warehouseA = WarehouseFactory.getWarehouse();
        Warehouse warehouseB = WarehouseFactory.getWarehouseB();

        warehouseA.setWarehouseManager(UserFactory.getUserEntityA());
        warehouseB.setWarehouseManager(UserFactory.getUserEntityB());

        Section sectionFreshA = SectionFactory.getFreshSection();
        sectionFreshA.setWarehouse(warehouseA);
        Section sectionFreshB = SectionFactory.getFreshSection();
        sectionFreshB.setWarehouse(warehouseB);

        InboundOrder inboundOrderA = new InboundOrder(null, null, null, sectionFreshA);
        InboundOrder inboundOrderB = new InboundOrder(null, null, null, sectionFreshB);

        Batch batchA = new Batch("batch1", 10F, 8, 13, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 15F, product, inboundOrderA);
        Batch batchB = new Batch("batch2", 10F, 8, 2, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 15F, product, inboundOrderB);
        product.setBatchList(Arrays.asList(batchA, batchB));

        Mockito.when(authService.authenticated()).thenReturn(UserFactory.getUserSS(UserFactory.getUserEntityA()));
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductQuantityResponse result = warehouseService.getProductQuantity(product.getId());

        assertEquals(product.getId(), result.getProductId());
        assertEquals(2, result.getWarehouses().size());
    }

    @Test
    public void testUserNotAuthorized(){
        Mockito.when(authService.authenticated()).thenReturn(UserFactory.getUserSS(UserFactory.getUserNonAuthorized()));
        assertThrows(UserNotAllowedException.class, () -> {
            warehouseService.getProductQuantity("123");
        });
    }

    @Test
    public void testProductNotFoundException(){
        Mockito.when(authService.authenticated()).thenReturn(UserFactory.getUserSS(UserFactory.getUserEntityA()));
        Mockito.when(productRepository.findById("123")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {
            warehouseService.getProductQuantity("123");
        });
    }
}
