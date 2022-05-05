package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, String> {
    Product findByName(String name);
}
