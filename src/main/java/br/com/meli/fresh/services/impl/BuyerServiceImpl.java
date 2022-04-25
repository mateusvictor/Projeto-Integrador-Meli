package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.repository.IBuyerRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuyerServiceImpl implements ICrudService<Buyer> {

    private final IBuyerRepository repository;

    @Override
    public Buyer create(Buyer buyer) {
        return null;
    }

    @Override
    public Buyer update(String id, Buyer buyer) {
        return null;
    }

    @Override
    public Buyer getById(String id) {
        return null;
    }

    @Override
    public Page<Buyer> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
