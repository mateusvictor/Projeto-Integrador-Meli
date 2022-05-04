package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.ProductComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductCommentRepository extends JpaRepository<ProductComment, String> {
    Page<ProductComment> findAllByProduct(Product product, Pageable pageable);

}
