package br.com.meli.fresh.unit;

import br.com.meli.fresh.factory.ProductCommentFactory;
import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.ProductComment;
import br.com.meli.fresh.repository.IProductCommentRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.impl.ProductCommentServiceImpl;
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)

public class ProductCommentServiceTest {
    @Mock
    private IProductCommentRepository commentRepository;
    @Mock
    private IProductRepository productRepository;
    @Mock
    private UserAuthenticatedService authService;

    @InjectMocks
    private ProductCommentServiceImpl service;


    private UserSpringSecurity fakeAuthenticatedUSer() {
        // Simulation of a authenticated user by Mockito
        GrantedAuthority admUser = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority warehouseUser = new SimpleGrantedAuthority("ROLE_WAREHOUSEMANAGER");
        GrantedAuthority buyerUser = new SimpleGrantedAuthority("ROLE_BUYER");
        GrantedAuthority sellerUser = new SimpleGrantedAuthority("ROLE_SELLER");
        List<GrantedAuthority> list = List.of(admUser, sellerUser, buyerUser, warehouseUser);
        UserSpringSecurity u = new UserSpringSecurity(
                "user1",
                "admin",
                "password",
                Collections.checkedList(list, GrantedAuthority.class)
        );
        Mockito.when(authService.authenticated()).thenReturn(u);
        return u;
    }

    private Product basicSetup(){
        Product product = ProductFactory.getFreshProductA();
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        return product;
    }

    @Test
    public void mustCreateComment(){
        this.fakeAuthenticatedUSer();
        this.basicSetup();
        ProductComment comment = ProductCommentFactory.createCommentA();
        comment.setId("1");
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(comment);
        ProductComment commentCreated = service.create(comment,"1");
        assertEquals(comment.getId(), commentCreated.getId());


    }

    @Test
    public void mustGetAllCommentsByProduct(){
        Product product = this.basicSetup();
        Page<ProductComment> page = ProductCommentFactory.createPageProductCommment();
        Mockito.when(commentRepository.findAllByProduct(product, Pageable.unpaged())).thenReturn(page);
        Page<ProductComment> pageReturned = service.getAll("1",Pageable.unpaged());
        assertEquals(pageReturned.getTotalElements(), page.getTotalElements());


    }

    @Test
    public void mustGetCommentByProductAndId(){
        this.basicSetup();
        Product product = ProductFactory.getProductCommented();
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        ProductComment comment = service.getById("1", "1");
        assertEquals(comment.getDescription(), "testA");

    }

    @Test
    public void mustDeleteComment(){
        this.fakeAuthenticatedUSer();
        this.basicSetup();
        Mockito.when(commentRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(ProductCommentFactory.createCommentA()));
        service.delete("1","1");
        Mockito.verify(commentRepository, Mockito.times(1)).delete(Mockito.any());
    }

//    @Test
//    public void mustThrowCommentNotFoundException(){
//        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(ProductFactory.getFrozenProductA()));
//        Mockito.when(commentRepository.findById(Mockito.any())).thenThrow(CommentNotFoundException.class);
//        assertThrows(CommentNotFoundException.class,()-> service.getById("1", "1"));
//    }




}
