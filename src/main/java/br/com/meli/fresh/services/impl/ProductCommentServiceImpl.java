package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.ProductComment;
import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.repository.IProductCommentRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.IProductCommentService;
import br.com.meli.fresh.services.exception.CommentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class ProductCommentServiceImpl implements IProductCommentService<ProductComment> {

    private final IProductCommentRepository commentRepository;
    private final IProductRepository productRepository;
    private final UserAuthenticatedService authService;

    @Override
    public ProductComment create(ProductComment productComment, String idProduto) {
        this.validateUser();
        Product product = this.findProduct(idProduto);
        productComment.setProduct(product);
        productComment.setCommentDateTime(LocalDateTime.now());
        productComment.setId(null);
        return this.commentRepository.save(productComment);
    }


    @Override
    public ProductComment getById(String idProduto, String idComment) {
        Product product = this.findProduct(idProduto);
        return product.getComments().stream().filter((c)-> c.getId().equals(idComment)).findFirst().orElseThrow(()-> new CommentNotFoundException("Comment not found!"));
    }

    @Override
    public Page<ProductComment> getAll(String idProduto, Pageable pageable) {
        Product product = this.findProduct(idProduto);
        return this.commentRepository.findAllByProduct(product, pageable);

    }

    @Override
    public void delete(String idProduto, String idComment) {
        ProductComment comment = this.commentRepository.findById(idComment).orElseThrow(()-> new CommentNotFoundException("Comment not found!"));
        this.validateUser(comment.getBuyer().getId());
        this.findProduct(idProduto);
        this.commentRepository.delete(comment);
    }

    private void validateUser(String idBuyer) {
        UserSpringSecurity userClient = authService.authenticated();
        if(userClient == null || (!userClient.hasRole(Role.ADMIN) && !userClient.hasRole(Role.BUYER))||!userClient.getId().equals(idBuyer)){
            throw new UserNotAllowedException("User not allowed!");
        }
    }

    private void validateUser() {
        UserSpringSecurity userClient = authService.authenticated();
        if(userClient == null || (!userClient.hasRole(Role.ADMIN) && !userClient.hasRole(Role.BUYER))){
            throw new UserNotAllowedException("User not allowed!");
        }
    }

    private Product findProduct(String idProduto) {
        return this.productRepository.findById(idProduto).orElseThrow(()-> new ProductNotFoundException(idProduto));
    }
}

