package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.exception.ProductAlreadyExistsException;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.model.exception.ProductsNotFoundException;
import br.com.meli.fresh.model.filter.ProductFilter;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
        return repository.save(updatingProduct(product, productToBeUpdated));
    }

    @Override
    public Product getById(String id) {
        return repository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Product> getAll(ProductFilter filter, Pageable pageable) {
        Page<Product> pages = repository.findAll(pageable);
        if(filter.getCategory() != null) pages = new PageImpl<Product>(pages.stream().filter(p -> p.getCategory().equals(filter.getCategory())).collect(Collectors.toList()));

        if(pages.getTotalElements() == 0) throw new ProductsNotFoundException();
        return pages;
    }

    @Override
    public void delete(String id) {
        Product p = repository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        p.setIsActive(false);
        repository.save(p);
    }

    private Product updatingProduct(Product newProduct, Product olderProduct) {
        if(newProduct.getName() != null) olderProduct.setName(newProduct.getName());
        if(newProduct.getCategory() != null) olderProduct.setCategory(newProduct.getCategory());
        if(newProduct.getMinTemperature() != null) olderProduct.setMinTemperature(newProduct.getMinTemperature());
        if(newProduct.getMaxTemperature() != null) olderProduct.setMaxTemperature(newProduct.getMaxTemperature());
        if(newProduct.getWeight() != null) olderProduct.setWeight(newProduct.getWeight());
        return olderProduct;
    }
}
