package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.model.exception.ProductsNotFoundException;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.filter.ProductFilter;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ICrudService<Product> {

    private final IProductRepository repository;
    private final IUserRepository userRepository;
    private final UserAuthenticatedService auth;

    @Override
    public Product create(Product product) {

        UserSpringSecurity warehouse = auth.authenticated();
        if(warehouse == null && !warehouse.hasRole(Role.ADMIN) || !warehouse.hasRole(Role.ADMIN)) {
            throw new UserNotAllowedException("This user authenticated has not authorization to create a product!");
        }

        userRepository.findById(product.getSeller().getId()).orElseThrow(() -> new UserNotFoundException(product.getSeller().getId()));

        if(!userRepository.findById(product.getSeller().getId()).get().getRoles().contains(Role.SELLER)) {
            throw new UserNotAllowedException("This user is not a seller!");
        }

       // Vinculating the batches with the product
        if(product.getBatchList() != null && product.getBatchList().size()  != 0) {
            product.setBatchList(product.getBatchList().stream().map(batch -> {
                 batch.setProduct(product);
                 return batch;
            }).collect(Collectors.toList()));
        }

        return repository.save(product);

    }

    @Override
    public Product update(String id, Product product) {
        UserSpringSecurity warehouse = auth.authenticated();
        if(warehouse == null && !warehouse.hasRole(Role.ADMIN) || !warehouse.hasRole(Role.ADMIN)) {
            throw new UserNotAllowedException("This user authenticated has not authorization to create a product!");
        }

        Product productToBeUpdated = repository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        if(product.getSeller() != null) {
            userRepository.findById(product.getSeller().getId()).orElseThrow(() -> new UserNotFoundException(product.getSeller().getId()));
            if(!userRepository.findById(product.getSeller().getId()).get().getRoles().contains(Role.SELLER)) {
                throw new UserNotAllowedException("This user is not a seller!");
            }
        }
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
        if(filter.getCategory() != null) {
            List<Product> list = pages.stream().filter(p -> {
                if(p.getCategory() != null) return p.getCategory().equals(filter.getCategory());
                return false;
            }).collect(Collectors.toList());
            pages = new PageImpl<Product>(list);
        }
        if(filter.getDuo_date() != null) {
            UserSpringSecurity u = auth.authenticated();
            if(u == null && !u.hasRole(Role.ADMIN) || !u.hasRole(Role.WAREHOUSEMANAGER)) {
                throw new UserNotAllowedException("User do not have authorization to use the duo date filter!");
            }
            pages.stream().forEach(p -> {
                List<Batch> batchesFiltered = p.getBatchList().stream().filter(b -> {
                    if(filter.getDuo_date().equals("0")) return b.getDueDate().isAfter(LocalDate.now());
                    if(filter.getDuo_date().equals("1")) return b.getDueDate().isBefore(LocalDate.now());
                    return false;
                }).collect(Collectors.toList());
                p.setBatchList(batchesFiltered);
            });
        }

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
        if(newProduct.getSeller() != null) olderProduct.setSeller(newProduct.getSeller());
        return olderProduct;
    }
}
