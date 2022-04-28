package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.*;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.CartNotFoundException;
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

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
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

    private final int currentQuantity = 10;
    private final int itemQuantity = 1;

    @InjectMocks
    private CartServiceImpl cartService;

    public Batch createBatch() {
        Batch b = new Batch();
        b.setCurrentQuantity(currentQuantity);
        b.setDueDate(LocalDate.now().plusMonths(1));
        return b;
    }

    public Product createProduct(Batch b) {
        Product p = new Product();
        p.setBatchList(Collections.singletonList(b));
        p.setId("1");
        p.setName("a");
        return p;
    }


    public Product creationProductSetup() {
        Batch b = createBatch();
        Product p = createProduct(b);
        b.setProduct(p);
        return p;
    }


    public Product creationProductNotFoundSetup() {
        Batch b = createBatch();
        Product p = createProduct(b);
        b.setProduct(p);
        Mockito.when(productRepository.findById("1")).thenThrow(ProductNotFoundException.class);
        return p;
    }

    public Product creationProductDueDateNotSufficientSetup() {
        Batch b = createBatch();
        Product p = createProduct(b);
        b.setProduct(p);
        b.setDueDate(LocalDate.now());
        b.setProduct(p);
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        return p;
    }

    public Product creationProductQuantityNotSufficientSetup() {
        Batch b = createBatch();
        b.setCurrentQuantity(0);
        Product p = createProduct(b);
        b.setProduct(p);
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        return p;
    }

    public Buyer creationBuyerSetup() {
        Buyer b = new Buyer();
        b.setId("1");
        Mockito.when(buyerRepository.findById("1")).thenReturn(Optional.of(b));
        return b;
    }

    public Buyer creationBuyerNotFoundSetup() {
        Buyer b = new Buyer();
        b.setId("1");
        Mockito.when(buyerRepository.findById("1")).thenThrow(BuyerNotFoundException.class);
        return b;
    }

    public Cart creationCartSetup(Buyer b, Product p) {
        Cart c = new Cart();
        CartItem i = new CartItem();
        i.setProduct(p);
        i.setQuantity(itemQuantity);
        c.setId("1");
        c.setBuyer(b);
        c.setItems(Collections.singletonList(i));
        return c;
    }

    public void decrementOfBatchProductQuantitySetup(List<Batch> b) {
        Mockito.when(batchRepository.findAllByProduct_Id("1")).thenReturn(b);
        Mockito.when(batchRepository.saveAll(Mockito.any())).thenReturn(b);
    }


    @Test
    public void testCreateCart() {
        Product p = this.creationProductSetup();
        Buyer b = this.creationBuyerSetup();
        Cart c = this.creationCartSetup(b, p);
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        Mockito.when(cartRepository.save(c)).thenReturn(c);
        assertNotNull(cartService.create(c));
    }

    @Test
    public void testProductNotFount() {
        Product p = this.creationProductNotFoundSetup();
        Buyer b = this.creationBuyerSetup();
        Cart c = this.creationCartSetup(b, p);
        assertThrows(ProductNotFoundException.class, () -> cartService.create(c));
    }

    @Test
    public void testBuyerNotFound() {
        Product p = this.creationProductSetup();
        Buyer b = this.creationBuyerNotFoundSetup();
        Cart c = this.creationCartSetup(b, p);
        assertThrows(BuyerNotFoundException.class, () -> cartService.create(c));
    }

    @Test
    public void testDueDateIsNoLessThanThreeWeeks() {
        Product p = this.creationProductDueDateNotSufficientSetup();
        Buyer b = this.creationBuyerSetup();
        Cart c = this.creationCartSetup(b, p);
        assertThrows(InsufficientQuantityOfProductException.class, () -> cartService.create(c));
    }

    @Test
    public void testQuantityNotValid() {
        Product p = this.creationProductQuantityNotSufficientSetup();
        Buyer b = this.creationBuyerSetup();
        Cart c = this.creationCartSetup(b, p);
        assertThrows(InsufficientQuantityOfProductException.class, () -> cartService.create(c));
    }

    @Test
    public void testUpdateCart() {
        Product p = this.creationProductSetup();
        Buyer b = new Buyer();
        Cart c = this.creationCartSetup(b, p);
        c.setCartStatus(CartStatus.OPEN);

        Mockito.when(cartRepository.findById("1")).thenReturn(Optional.of(c));
        Mockito.when(cartRepository.save(c)).thenReturn(c);
        decrementOfBatchProductQuantitySetup(p.getBatchList());
        assertNotNull(cartService.update(c.getId()));

        assertEquals(CartStatus.CLOSE, c.getCartStatus());
        int totalQuantity = p.getBatchList().stream().reduce(0, (acc, sub) ->
                acc + sub.getCurrentQuantity(), Integer::sum);
        assertEquals(currentQuantity - itemQuantity, totalQuantity);

    }

    @Test
    public void testUpdateCartNotDecrementProductQuantity() {
        Product p = this.creationProductSetup();
        Buyer b = new Buyer();
        Cart c = this.creationCartSetup(b, p);
        c.setCartStatus(CartStatus.CLOSE);

        Mockito.when(cartRepository.findById("1")).thenReturn(Optional.of(c));
        assertNotNull(cartService.update(c.getId()));

        assertEquals(CartStatus.CLOSE, c.getCartStatus());
        int totalQuantity = p.getBatchList().stream().reduce(0, (acc, sub) ->
                acc + sub.getCurrentQuantity(), Integer::sum);
        assertEquals(currentQuantity, totalQuantity);

    }

    @Test
    public void testCartNotFoundThrowsCartNotFoundException() {
        Mockito.when(cartRepository.findById("1")).thenThrow(CartNotFoundException.class);
        assertThrows(CartNotFoundException.class, () -> cartService.update("1"));
    }

    @Test
    public void testGetByIdReturnCart() {
        Cart c = new Cart();
        c.setId("1");
        Mockito.when(cartRepository.findById("1")).thenReturn(Optional.of(c));
        cartService.getById("1");
        assertNotNull(c);
        assertEquals("1", c.getId());
    }

    @Test
    public void testGetByIdThrowsCartNotFoundException() {
        Mockito.when(cartRepository.findById("1")).thenThrow(CartNotFoundException.class);
        assertThrows(CartNotFoundException.class, () -> cartService.getById("1"));
    }


}
