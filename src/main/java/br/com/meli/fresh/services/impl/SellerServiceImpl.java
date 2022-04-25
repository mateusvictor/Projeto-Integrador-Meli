package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Seller;
import br.com.meli.fresh.repository.ISellerRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements ICrudService<Seller> {

    private final ISellerRepository repository;

    @Override
    public Seller create(Seller seller) {
        return null;
    }

    @Override
    public Seller update(String id, Seller seller) {
        return null;
    }

    @Override
    public Seller getById(String id) {
        return null;
    }

    @Override
    public Page<Seller> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
