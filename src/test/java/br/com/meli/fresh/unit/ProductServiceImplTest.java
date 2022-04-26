package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private static IProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    public Product creationSetup(){
        Product p = new Product();
        p.setName("ProductTeste");

        Mockito.when(productRepository.save(p)).thenReturn(p);

        return p;
    }

    public Product getSetup(){
        Product p = new Product();
        p.setName("ProductTeste");
        p.setId("1");

        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        Mockito.doReturn(new ProductNotFoundException("2")).when(productRepository.findById("2"));
        return p;
    }

    @Test
    public void testProductCreation(){
        Product p = this.creationSetup();
        assertNotNull(productService.create(p));

    }

    @Test
    public void testGetProduct(){
        Product p = this.getSetup();
        assertNotNull(productService.getById("1"));
    }

    @Test
    public void testNotFoundProduct(){
        Product p = this.getSetup();
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getById(null);
        });

    }



}
