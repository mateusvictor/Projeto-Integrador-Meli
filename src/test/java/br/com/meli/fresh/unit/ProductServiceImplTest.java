package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.model.exception.ProductsNotFoundException;
import br.com.meli.fresh.model.filter.ProductFilter;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private static IProductRepository productRepository;

    @Mock
    private static UserAuthenticatedService authService;

    @Mock
    private static IUserRepository userRepository;

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
