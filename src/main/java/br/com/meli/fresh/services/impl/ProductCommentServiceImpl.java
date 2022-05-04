package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.ProductComment;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.repository.IProductCommentRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.services.ICrudService;
import br.com.meli.fresh.services.IProductCommentService;
import br.com.meli.fresh.services.exception.CommentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductCommentServiceImpl implements IProductCommentService<ProductComment> {

    private final IProductCommentRepository commentRepository;
    private final IProductRepository productRepository;

    @Override
    public ProductComment create(ProductComment productComment, String idProduto) {
        Product product = this.findProduct(idProduto);
        productComment.setProduct(product);
        productComment.setCommentDateTime(LocalDateTime.now());
        productComment.setId(null);
        return this.commentRepository.save(productComment);
    }

    private Product findProduct(String idProduto) {
        return this.productRepository.findById(idProduto).orElseThrow(()-> new ProductNotFoundException(idProduto));
    }

    @Override
    public ProductComment getById(String idProduto, String idComment) {
        Product product = this.findProduct(idProduto);
        return product.getComments().stream().filter((c)-> c.getId().equals(idComment)).findFirst().orElseThrow(()-> new CommentNotFoundException("Comment not found!"));
//        return this.commentRepository.findById(idComment).orElseThrow(()-> new ProductNotFoundException(idProduto));4
    }

    @Override
    public Page<ProductComment> getAll(String idProduto, Pageable pageable) {
        Product product = this.findProduct(idProduto);
        return this.commentRepository.findAllByProduct(product, pageable);

    }

    @Override
    public void delete(String idProduto, String idComment) {
        this.findProduct(idProduto);
        ProductComment comment = this.commentRepository.findById(idComment).orElseThrow(()-> new CommentNotFoundException("Comment not found!"));

        this.commentRepository.delete(comment);
    }
}

