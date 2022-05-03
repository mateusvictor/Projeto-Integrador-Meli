package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.dto.response.ProductQuantityResponse;
import br.com.meli.fresh.dto.response.WarehouseProductQuantity;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.model.exception.*;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements ICrudService<Warehouse> {

    private final IWarehouseRepository repository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final UserAuthenticatedService auth;

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


    private void verifyManager(Warehouse warehouse) {
        if(warehouse.getWarehouseManager()!=null) {
            if(!this.userRepository.findById(warehouse.getWarehouseManager().getId()).orElseThrow(
                    ()->new UserNotFoundException(warehouse.getWarehouseManager().getId())).getRoles().contains(Role.WAREHOUSEMANAGER)){
                throw new UserNotAllowedException("User id "+ warehouse.getWarehouseManager().getId()+" not allowed to be warehouse manager");
            }
            Warehouse warehouseWithUser = this.repository.findWarehouseByWarehouseManager(warehouse.getWarehouseManager());

            if ( warehouseWithUser!= null) {
                if(warehouse.getId()!=null && !warehouseWithUser.getId().equals(warehouse.getId())){
                    throw new WarehouseManagerAlreadyDefined("Warehouse manager already defined in another warehouse");
                }else if(warehouse.getId()==null){
                    throw new WarehouseManagerAlreadyDefined("Warehouse manager already defined in another warehouse");
                }
            }
        }
    }

    public ProductQuantityResponse getProductQuantity(String productId){
        UserSpringSecurity user = auth.authenticated();

        if (user == null || (!user.hasRole(Role.WAREHOUSEMANAGER) && !user.hasRole(Role.ADMIN)))
            throw new UserNotAllowedException("You don't have permission to access this endpoint");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        List<WarehouseProductQuantity> warehouseList = new ArrayList<>();

        for (Batch batch : product.getBatchList()){
            // Each batch is in one order, each order has one section and one section has a warehouse
            Warehouse warehouse = batch.getInboundOrder().getSection().getWarehouse();
            warehouseList.add(new WarehouseProductQuantity(warehouse.getId(), batch.getCurrentQuantity()));
        }

        return new ProductQuantityResponse(productId, warehouseList);
    }

}
