package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.dto.response.ProductQuantityResponse;
import br.com.meli.fresh.dto.response.WarehouseProductQuantity;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.Warehouse;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements ICrudService<Warehouse> {

    private final IWarehouseRepository repository;
    private final IUserRepository userRepository;
    private final UserAuthenticatedService authService;
    private final IProductRepository productRepository;
    private final UserAuthenticatedService auth;

    @Override
    public Warehouse create(Warehouse warehouse) {
        this.validateUser();
        this.verifyManagerCreate(warehouse);
        this.setWarehouseToSection(warehouse);
        return this.repository.save(warehouse);
    }

    @Override
    public Warehouse update(String id, Warehouse warehouse) {
        this.validateUser(warehouse.getWarehouseManager().getId());
        this.repository.findById(id).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found!"));
        warehouse.setId(id);
        this.verifyManagerUpdate(warehouse);
        this.setWarehouseToSection(warehouse);
        return this.repository.save(warehouse);
    }

    @Override
    public Warehouse getById(String id) {
        this.validateUser();
        return this.repository.findById(id).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found!"));
    }

    @Override
    public Page<Warehouse> getAll(Pageable pageable) {
        this.validateUser();
        return this.repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        Warehouse warehouse = this.repository.findById(id).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found!"));
        this.validateUser(warehouse.getWarehouseManager().getId());
        this.repository.delete(warehouse);
    }

    private void verifyRole(Warehouse warehouse) {
        if (!this.userRepository.findById(warehouse.getWarehouseManager().getId()).orElseThrow(
                () -> new UserNotFoundException(warehouse.getWarehouseManager().getId())).getRoles().contains(Role.WAREHOUSEMANAGER)) {
            throw new UserNotAllowedException("User id " + warehouse.getWarehouseManager().getId() + " not allowed to be warehouse manager");
        }
    }

    //Verify current conditions of the manager set on create or update
    private void verifyManagerCreate(Warehouse warehouse) {
        verifyRole(warehouse);



        //Find a warehouse that with the manager specified on the request
        Warehouse warehouseWithUser = this.repository.findByWarehouseManager(warehouse.getWarehouseManager());
        if (warehouseWithUser != null) { //verify if is a create of a warehouse and throws exception;
           throw new WarehouseManagerAlreadyDefined("Warehouse manager already defined in another warehouse");
        }
    }


    private void verifyManagerUpdate(Warehouse warehouse) {
        verifyRole(warehouse);

        //Find a warehouse that with the manager specified on the request
        Warehouse warehouseWithUser = this.repository.findByWarehouseManager(warehouse.getWarehouseManager());
        if (warehouseWithUser != null) {
            //verify if is an update of an existing warehouse on db and throws an exception
            if (warehouse.getId() != null && !warehouseWithUser.getId().equals(warehouse.getId())) {
                throw new WarehouseManagerAlreadyDefined("Warehouse manager already defined in another warehouse");
            }
        }
    }

    //set a warehouse to each section on the section list
    private void setWarehouseToSection(Warehouse warehouse) {
        warehouse.setSectionList(warehouse.getSectionList().stream().map(section -> {
            section.setWarehouse(warehouse);
            return section;
        }).collect(Collectors.toList()));
    }

    private void validateUser(){
        UserSpringSecurity userClient = authService.authenticated();
        if(userClient == null || (!userClient.hasRole(Role.ADMIN) && !userClient.hasRole(Role.WAREHOUSEMANAGER))){
            throw new UserNotAllowedException("User not allowed!");
        }
    }


    private void validateUser(String id) {
        UserSpringSecurity userClient = authService.authenticated();
        if (userClient == null || (!userClient.hasRole(Role.ADMIN) && !userClient.getId().equals(id))) {
            throw new UserNotAllowedException("User not allowed!");
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
