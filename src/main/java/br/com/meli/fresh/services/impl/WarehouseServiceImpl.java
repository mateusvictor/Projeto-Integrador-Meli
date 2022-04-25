package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements ICrudService<Warehouse> {

    private final IWarehouseRepository repository;

    @Override
    public Warehouse create(Warehouse warehouse) {
        return null;
    }

    @Override
    public Warehouse update(String id, Warehouse warehouse) {
        return null;
    }

    @Override
    public Warehouse getById(String id) {
        return null;
    }

    @Override
    public Page<Warehouse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
