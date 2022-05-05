package br.com.meli.fresh.unit;

import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.exception.SaleProductNotFoundException;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.services.impl.SaleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SaleServiceImplTest {

    @Mock
    private IBatchRepository batchRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    public Batch createBatch() {
        Batch b = new Batch();
        b.setCurrentQuantity(5);
        b.setDueDate(LocalDate.now().plusWeeks(1));
        return b;
    }

    public Product createProduct(Batch b) {
        Product p = new Product();
        p.setBatchList(Collections.singletonList(b));
        p.setId("1");
        p.setName("a");
        return p;
    }

    @Test
    public void testSaleProductNotFoundException(){
        Batch b = createBatch();
        Product p = createProduct(b);
        b.setProduct(p);
        p.setBatchList(List.of(b));

        Mockito.when(batchRepository.findAllByDueDateBetweenOrderByDueDateAsc(Mockito.any(), Mockito.any()))
        .thenReturn(new ArrayList<>());
        assertThrows(SaleProductNotFoundException.class, () -> saleService.getAllProductsDueDate());
    }

    @Test
    public void testSaleProductDiscountAndDueDate(){

        Product p1 = ProductFactory.createProduct();
        Batch b1 = createBatch();
        b1.setDueDate(LocalDate.now().plusWeeks(1).minusDays(1));
        p1.setPrice(BigDecimal.valueOf(10));
        p1.setId("1");
        b1.setProduct(p1);

        Product p2 = ProductFactory.createProduct();
        Batch b2 = createBatch();
        b2.setDueDate(LocalDate.now().plusWeeks(2).minusDays(1));
        p2.setPrice(BigDecimal.valueOf(10));
        p2.setId("2");
        b2.setProduct(p2);

        Product p3 = ProductFactory.createProduct();
        Batch b3 = createBatch();
        b3.setDueDate(LocalDate.now().plusWeeks(3).minusDays(1));
        p3.setPrice(BigDecimal.valueOf(10));
        p3.setId("3");
        b3.setProduct(p3);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(1);

        Mockito.when(batchRepository.findAllByDueDateBetweenOrderByDueDateAsc(Mockito.any(), Mockito.any()))
        .thenReturn(Arrays.asList(b1,b2,b3));
        List<Batch> list = saleService.getAllProductsDueDate();
        Batch result = list.get(0);
        assertEquals(BigDecimal.valueOf(8.00).setScale(2,RoundingMode.HALF_EVEN), result.getProduct().getPrice());
        Batch result2 = list.get(1);
        assertEquals(BigDecimal.valueOf(9.00).setScale(2,RoundingMode.HALF_EVEN), result2.getProduct().getPrice());
        Batch result3 = list.get(2);
        assertEquals(BigDecimal.valueOf(9.50).setScale(2,RoundingMode.HALF_EVEN), result3.getProduct().getPrice());
    }
}
