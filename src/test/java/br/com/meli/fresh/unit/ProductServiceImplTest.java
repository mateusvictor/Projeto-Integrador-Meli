package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.exception.ProductAlreadyExistsException;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
        return p;
    }

    public Product productAlreadyExistsSetup() {
        Product p = new Product();
        p.setName("Product Test");
        Mockito.when(productRepository.save(p)).thenThrow(ProductAlreadyExistsException.class);
        return p;
    }

    public Page<Product> productListSetup() {
        List<Product> list = List.of(
                new Product(),
                new Product(),
                new Product(),
                new Product(),
                new Product()
        );

        Page<Product> page = new PageImpl<Product>(list);
        Pageable pageable = Pageable.unpaged();
        Mockito.when(productRepository.findAll(pageable)).thenReturn(page);
        return page;
    }

    public Product updateProductSetup() {
        Product p = new Product();
        p.setName("Pizza");
        p.setId("1");
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
        p.setName("Ice cream");
        Mockito.when(productRepository.save(p)).thenReturn(p);
        return p;
    }

    public Product deleteProductSetup() {
        Product p = new Product();
        p.setIsActive(false);
        p.setId("1");
        Mockito.when(productRepository.findById("1")).thenReturn(Optional.of(p));
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
        Mockito.when(productRepository.findById("2")).thenThrow(ProductNotFoundException.class);
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getById("2");
        });
    }

    @Test
    public void testProductAlreadyExists() {
        Product p = this.productAlreadyExistsSetup();
        assertThrows(ProductAlreadyExistsException.class, () -> {
            productService.create(p);
        });
    }

    @Test
    public void testGetAllProducts() {
        this.productListSetup();
        Pageable pageable = Pageable.unpaged();
        assertEquals(5, productService.getAll(pageable).getSize());
    }

    @Test
    public void testUpdateProduct() {
        Product p = this.updateProductSetup();
        assertEquals(productService.update("1", p).getName(), p.getName());
    }

    @Test
    public void testDeleteProduct() {
        Product p = this.deleteProductSetup();
        productService.delete(p.getId());
        assertEquals(p.getIsActive(), false);
    }
}
