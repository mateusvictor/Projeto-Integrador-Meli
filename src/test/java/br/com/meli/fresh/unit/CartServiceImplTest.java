package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.*;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.InsufficientQuantityOfProductException;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IBuyerRepository;
import br.com.meli.fresh.repository.ICartRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.services.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private ICartRepository cartRepository;
    @Mock
    private IProductRepository productRepository;
    @Mock
    private IBuyerRepository buyerRepository;
    @Mock
    private IBatchRepository batchRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    public Product creationProductSetup(){
        Batch b = new Batch();
        b.setCurrentQuantity(10);
        b.setDueDate(LocalDate.now().plusMonths(1));
        Product p = new Product();
        p.setBatchList(Arrays.asList(b));
        p.setId("1");
        p.setName("a");
        b.setProduct(p);
        return p;
    }


    public Product creationProductNotFoundSetup(){
        Batch b = new Batch();
        b.setCurrentQuantity(10);
        b.setDueDate(LocalDate.now().plusMonths(1));
        Product p = new Product();
        p.setBatchList(Arrays.asList(b));
        p.setId("1");
        p.setName("a");
        b.setProduct(p);
        Mockito.when(productRepository.findById("1")).thenThrow(ProductNotFoundException.class);
        return p;
    }

    public Product creationProductDueDateNotSufficientSetup(){
        Batch b = new Batch();
        b.setCurrentQuantity(10);
        b.setDueDate(LocalDate.now());
        Product p = new Product();
        p.setBatchList(Arrays.asList(b));
        p.setId("1");
        p.setName("a");
        b.setProduct(p);
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        return p;
    }

    public Product creationProductQuantityNotSufficientSetup(){
        Batch b = new Batch();
        b.setCurrentQuantity(0);
        b.setDueDate(LocalDate.now().plusMonths(1));
        Product p = new Product();
        p.setBatchList(Arrays.asList(b));
        p.setId("1");
        p.setName("a");
        b.setProduct(p);
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        return p;
    }

    public Buyer creationBuyerSetup(){
        Buyer b = new Buyer();
        b.setId("1");
        Mockito.when(buyerRepository.findById("1")).thenReturn(Optional.of(b));
        return b;
    }

    public Buyer creationBuyerNotFoundSetup(){
        Buyer b = new Buyer();
        b.setId("1");
        Mockito.when(buyerRepository.findById("1")).thenThrow(BuyerNotFoundException.class);
        return b;
    }

    public Cart creationCartSetup(Buyer b, Product p){
        Cart c = new Cart();
        CartItem i = new CartItem();
        i.setProduct(p);
        i.setQuantity(1);
        c.setId("1");
        c.setBuyer(b);
        c.setItems(Arrays.asList(i));
        return c;
    }

    @Test
    public void testCreateCart(){
        Product p = this.creationProductSetup();
        Buyer b = creationBuyerSetup();
        Cart c = creationCartSetup(b, p);
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        Mockito.when(cartRepository.save(c)).thenReturn(c);
        assertNotNull(cartService.create(c));
        /*
        int totalQuantity = p.getBatchList().stream().reduce(0, (acc, sub) ->
                acc + sub.getCurrentQuantity(), Integer::sum);
        assertEquals(9, totalQuantity);

         */
    }

    @Test
    public void testProductNotFount(){
        Product p = this.creationProductNotFoundSetup();
        Buyer b = creationBuyerSetup();
        Cart c = creationCartSetup(b, p);
        assertThrows(ProductNotFoundException.class, () -> {
            cartService.create(c);
        });
    }

    @Test
    public void testBuyerNotFound(){
        Product p = this.creationProductSetup();
        Buyer b = this.creationBuyerNotFoundSetup();
        Cart c = creationCartSetup(b, p);
        assertThrows(BuyerNotFoundException.class, () -> {
            cartService.create(c);
        });
    }

    @Test
    public void testDueDateIsNoLessThanThreeWeeks(){
        Product p = this.creationProductDueDateNotSufficientSetup();
        Buyer b = this.creationBuyerSetup();
        Cart c = this.creationCartSetup(b, p);
        assertThrows(InsufficientQuantityOfProductException.class, () -> {
            cartService.create(c);
        });
    }

    @Test
    public void testQuantityNotValid(){
        Product p = this.creationProductQuantityNotSufficientSetup();
        Buyer b = this.creationBuyerSetup();
        Cart c = this.creationCartSetup(b, p);
        assertThrows(InsufficientQuantityOfProductException.class, () -> {
            cartService.create(c);
        });

    }
}
