package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.model.exception.WarehouseManagerAlreadyDefined;
import br.com.meli.fresh.model.exception.WarehouseNotFoundException;
import br.com.meli.fresh.repository.IGeneralUserRepository;
import br.com.meli.fresh.repository.IWarehouseManagerRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements ICrudService<Warehouse> {

    private final IWarehouseRepository repository;
    private final IWarehouseManagerRepository warehouseManagerRepository;
    private final IGeneralUserRepository generalUserRepository;

    @Override
    public Warehouse create(Warehouse warehouse) {
        this.verifyManager(warehouse);
        warehouse.setSectionList(warehouse.getSectionList().stream().map(section-> {
            section.setWarehouse(warehouse);
            return  section;
        }).collect(Collectors.toList()));
        if(warehouse.getWarehouseManager()!=null){
           // warehouse.getWarehouseManager().setWarehouse(warehouse);
        }
        return this.repository.save(warehouse);
    }

    @Override
    public Warehouse update(String id, Warehouse warehouse) {
        this.repository.findById(id).orElseThrow(()-> new WarehouseNotFoundException("Warehouse not found!"));
        warehouse.setId(id);
        this.verifyManager(warehouse);
        warehouse.setSectionList(warehouse.getSectionList().stream().map(section-> {
            section.setWarehouse(warehouse);
            return  section;
        }).collect(Collectors.toList()));
        if(warehouse.getWarehouseManager()!=null){
           // warehouse.getWarehouseManager().setWarehouse(warehouse);
        }
        return this.repository.save(warehouse);
    }

    @Override
    public Warehouse getById(String id) {
        return this.repository.findById(id).orElseThrow(()-> new WarehouseNotFoundException("Warehouse not found!"));
    }

    @Override
    public Page<Warehouse> getAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        Warehouse warehouse = this.repository.findById(id).orElseThrow(()-> new WarehouseNotFoundException("Warehouse not found!"));
       // warehouse.getWarehouseManager().setWarehouse(null);
        this.repository.delete(warehouse);
    }


    private void verifyManager(Warehouse warehouse) {
        if(warehouse.getWarehouseManager()!=null){
//            if(this.repository.findByWarehouseManagerId(warehouse.getWarehouseManager().getId())
//
//            ){
//                throw new WarehouseManagerAlreadyDefined("Warehouse manager already defined to a warehouse");
//            }
        }
    }

}
