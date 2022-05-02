package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.exception.WarehouseManagerAlreadyDefined;
import br.com.meli.fresh.model.exception.WarehouseNotFoundException;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements ICrudService<Warehouse> {

    private final IWarehouseRepository repository;
    private final IUserRepository userRepository;

    @Override
    public Warehouse create(Warehouse warehouse) {
        this.verifyManager(warehouse);
        this.setWarehouseToSection(warehouse);
        return this.repository.save(warehouse);
    }


    @Override
    public Warehouse update(String id, Warehouse warehouse) {
        this.repository.findById(id).orElseThrow(()-> new WarehouseNotFoundException("Warehouse not found!"));
        warehouse.setId(id);
        this.verifyManager(warehouse);
        this.setWarehouseToSection(warehouse);
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
        this.repository.delete(this.repository.findById(id).orElseThrow(()-> new WarehouseNotFoundException("Warehouse not found!")));
    }

    //Verify current conditions of the manager set on create or update
    private void verifyManager(Warehouse warehouse) {
        if(warehouse.getWarehouseManager()!=null) {
            if(!this.userRepository.findById(warehouse.getWarehouseManager().getId()).orElseThrow(
                    ()->new UserNotFoundException(warehouse.getWarehouseManager().getId())).getRoles().contains(Role.WAREHOUSEMANAGER)){
                throw new UserNotAllowedException("User id "+ warehouse.getWarehouseManager().getId()+" not allowed to be warehouse manager");
            }
            //Find a warehouse that with the manager specified on the request
            Warehouse warehouseWithUser = this.repository.findWarehouseByWarehouseManager(warehouse.getWarehouseManager());
            if ( warehouseWithUser!= null) {
                //verify if is an update of an existing warehouse on db and throws an exception
                if(warehouse.getId()!=null && !warehouseWithUser.getId().equals(warehouse.getId())){
                    throw new WarehouseManagerAlreadyDefined("Warehouse manager already defined in another warehouse");
                //verify if is a create of a warehouse and throws exception;
                }else if(warehouse.getId()==null){
                    throw new WarehouseManagerAlreadyDefined("Warehouse manager already defined in another warehouse");
                }
            }
        }
    }

    //se a warehouse to each section on the section list
    private void setWarehouseToSection(Warehouse warehouse) {
        warehouse.setSectionList(warehouse.getSectionList().stream().map(section-> {
            section.setWarehouse(warehouse);
            return  section;
        }).collect(Collectors.toList()));
    }

}
