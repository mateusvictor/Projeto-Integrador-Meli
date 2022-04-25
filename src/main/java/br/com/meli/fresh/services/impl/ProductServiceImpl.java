package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.exception.ProductAlreadyExistsException;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
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
        Product p = repository.findByName(product.getName());
        // This if statement will check if a given product already exists in our database.ß If so, we will raise an error, and if not, we will create the product
        if(p != null) throw new ProductAlreadyExistsException();
        return repository.save(product);

    }

    @Override
    public Product update(String id, Product product) {

        Product productToBeUpdated = repository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        product.setId(productToBeUpdated.getId());
        return repository.save(product);


    }

    @Override
    public Product getById(String id) {

        return repository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        Product p = repository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        p.setIsActive(false);

    }
}
