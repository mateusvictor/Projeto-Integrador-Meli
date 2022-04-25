package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.WarehouseManager;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import br.com.meli.fresh.model.exception.WarehouseManagerNotFoundException;
import br.com.meli.fresh.repository.IWarehouseManagerRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WarehouseManagerServiceImpl implements ICrudService<WarehouseManager> {

    private final IWarehouseManagerRepository repository;

    @Override
    public WarehouseManager create(WarehouseManager warehouseManager) {

        try{
            return this.repository.save(warehouseManager);
        }catch(DataIntegrityViolationException e){
            throw new EmailAlreadyExistsException("Email already exists!");
        }
    }

    @Override
    public WarehouseManager update(String id, WarehouseManager warehouseManager) {

        warehouseManager.setId(id);
        this.repository.findById(id).orElseThrow(()-> new WarehouseManagerNotFoundException("Warehouse Manager not found!"));
        return this.repository.save(warehouseManager);
    }

    @Override
    public WarehouseManager getById(String id) {

        return this.repository.findById(id).orElseThrow(()-> new WarehouseManagerNotFoundException("Warehouse Manager not found!"));
    }



    @Override
    public Page<WarehouseManager> getAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        this.repository.delete(this.repository.findById(id).orElseThrow(()-> new WarehouseManagerNotFoundException("Warehouse Manager not found!")));
    }
}
