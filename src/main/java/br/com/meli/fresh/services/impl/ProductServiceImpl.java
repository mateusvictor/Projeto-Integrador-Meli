package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ICrudService<Product> {

    private final IProductRepository repository;

    @Override
    public Product create(Product product) {
        return null;
    }

    @Override
    public Product update(String id, Product product) {
        return null;
    }

    @Override
    public Product getById(String id) {
        return null;
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
