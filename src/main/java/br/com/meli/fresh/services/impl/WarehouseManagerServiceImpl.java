package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.WarehouseManager;
import br.com.meli.fresh.repository.IWarehouseManagerRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WarehouseManagerServiceImpl implements ICrudService<WarehouseManager> {

    private final IWarehouseManagerRepository repository;

    @Override
    public WarehouseManager create(WarehouseManager warehouseManager) {
        return null;
    }

    @Override
    public WarehouseManager update(String id, WarehouseManager warehouseManager) {
        return null;
    }

    @Override
    public Page<WarehouseManager> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
