package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.model.exception.ProductsNotFoundException;
import br.com.meli.fresh.model.filter.ProductFilter;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private static IProductRepository productRepository;

    @Mock
    private static UserAuthenticatedService authService;

    @Mock
    private static IUserRepository userRepository;

    @Mock
    private IBatchRepository batchRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private UserSpringSecurity fakeAuthenticatedUSer() {
        // Simulation of a authenticated user by Mockito
        GrantedAuthority admUser = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority warehouseUser = new SimpleGrantedAuthority("ROLE_WAREHOUSEMANAGER");
        GrantedAuthority buyerUser = new SimpleGrantedAuthority("ROLE_BUYER");
        GrantedAuthority sellerUser = new SimpleGrantedAuthority("ROLE_SELLER");
        List<GrantedAuthority> list = List.of(admUser, sellerUser, buyerUser, warehouseUser);
        UserSpringSecurity u = new UserSpringSecurity(
                "1",
                "admin",
                "password",
                Collections.checkedList(list, GrantedAuthority.class)
        );
        Mockito.when(authService.authenticated()).thenReturn(u);
        return u;
    }

    private User fakeSeller() {
        User u = new User();
        u.setRoles(Set.of(0));

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(u));
        return u;
    }

    public Product creationSetup(){
        Product p = new Product();
        p.setName("ProductTeste");
        p.setSeller(new User());
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

    public void productsNotFoundExceptionSetup() {
        Pageable pageable = Pageable.unpaged();
        Mockito.when(productRepository.findAll(pageable)).thenThrow(ProductsNotFoundException.class);
    }

    public Page<Product> productDuoDateFilterSetup() {
        // Creating new product with batches expired and not expired
        Product p1 = new Product();
        Batch b1p1 = new Batch();
        Batch b2p1 = new Batch();

        // Creating date expiration
        b1p1.setDueDate(LocalDate.of(2022, 01, 01));
        b2p1.setDueDate(LocalDate.of(2022, 12, 31));

        // Vinculating batches on product and the contrariwise;
        p1.setBatchList(new ArrayList<>());
        p1.getBatchList().add(b1p1);
        p1.getBatchList().add(b2p1);
        b1p1.setProduct(p1);
        b2p1.setProduct(p1);

        // Same process for the second product
        Product p2 = new Product();
        Batch b1p2 = new Batch();
        Batch b2p2 = new Batch();

        b1p2.setDueDate(LocalDate.of(2022, 01, 02));
        b2p2.setDueDate(LocalDate.of(2022, 12, 30));

        p2.setBatchList(new ArrayList<>());
        p2.getBatchList().add(b1p2);
        p2.getBatchList().add(b2p2);
        b1p2.setProduct(p2);
        b2p2.setProduct(p2);

        // Return the page with products
        Page<Product> pages = new PageImpl<>(List.of(p1, p2));
        Mockito.when(productRepository.findAll(Pageable.unpaged())).thenReturn(pages);

        return pages;
    }

    public Page<Product> productListSetup() {
        Product p1 = new Product();
        Product p2 = new Product();
        Product p3 = new Product();
        Product p4 = new Product();
        Product p5 = new Product();
        p1.setCategory("FF");
        p2.setCategory("FF");
        p3.setCategory("FF");
        p4.setCategory("FF");
        p5.setCategory("RF");
        List<Product> list = List.of(p1, p2, p3, p4, p5);

        Page<Product> page = new PageImpl<Product>(list);
        Pageable pageable = Pageable.unpaged();
        Mockito.when(productRepository.findAll(pageable)).thenReturn(page);
        return page;
    }

    public Product updateProductSetup() {
        Product p = new Product();
        p.setName("Pizza");
        p.setId("1");
        p.setSeller(new User());
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
        this.fakeAuthenticatedUSer();
        this.fakeSeller();
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
    public void testProductsWereNotFoundException() {
        this.productsNotFoundExceptionSetup();
        ProductFilter filter = new ProductFilter();
        assertThrows(ProductsNotFoundException.class, () -> {
            productService.getAll(filter, Pageable.unpaged());
        });
    }

    @Test
    public void testGetAllProducts() {
        this.productListSetup();
        Pageable pageable = Pageable.unpaged();
        ProductFilter filter = new ProductFilter();
        assertEquals(5, productService.getAll(filter, pageable).getSize());
    }

    @Test
    public void testFilterGetAllProducts() {
        this.productListSetup();
        Pageable pageable = Pageable.unpaged();
        ProductFilter filter = new ProductFilter();
        filter.setCategory("RF");
        assertEquals(1, productService.getAll(filter, pageable).getSize());

    }

    @Test
    public void testNotExpiredFilterProducts() {
        this.fakeAuthenticatedUSer();
        this.productDuoDateFilterSetup();
        Pageable pageable = Pageable.unpaged();
        ProductFilter filter = new ProductFilter();
        filter.setDuo_date("0");

        Page<Product> pagesNotExpired = productService.getAll(filter, pageable);
        List<Product> productsNotExpired = pagesNotExpired.stream().collect(Collectors.toList());

        assertEquals(1, productsNotExpired.get(0).getBatchList().size());
        assertEquals(0, productsNotExpired.get(0).getBatchList().get(0).getDueDate().compareTo(LocalDate.of(2022, 12, 31)));
    }

    @Test
    public void testExpiredFilterProducts() {
        this.fakeAuthenticatedUSer();
        this.productDuoDateFilterSetup();
        Pageable pageable = Pageable.unpaged();
        ProductFilter filter = new ProductFilter();

        filter.setDuo_date("1");
        Page<Product> pagesExpired = productService.getAll(filter, pageable);
        List<Product> productsExpired = pagesExpired.stream().collect(Collectors.toList());

        assertEquals(1, productsExpired.get(1).getBatchList().size());
        assertEquals(0, productsExpired.get(1).getBatchList().get(0).getDueDate().compareTo(LocalDate.of(2022, 01, 02)));
    }

    @Test
    public void testUpdateProduct() {
        this.fakeAuthenticatedUSer();
        this.fakeSeller();
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
