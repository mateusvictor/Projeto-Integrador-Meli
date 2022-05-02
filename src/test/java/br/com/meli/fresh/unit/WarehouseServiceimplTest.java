package br.com.meli.fresh.unit;

import br.com.meli.fresh.factory.WarehouseFactory;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.exception.WarehouseManagerAlreadyDefined;
import br.com.meli.fresh.model.exception.WarehouseNotFoundException;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.impl.WarehouseServiceImpl;
import br.com.meli.fresh.unit.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceimplTest {

    @Mock
    private static IWarehouseRepository repository;
    @Mock
    private static IUserRepository userRepository;

    @InjectMocks
    private WarehouseServiceImpl service;

    private Warehouse setupCreate(){
        User user = UserFactory.createWarehouseManagerDefault();
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(repository.findWarehouseByWarehouseManager(Mockito.any())).thenReturn(null);
        Mockito.when(repository.save(Mockito.any())).thenReturn(warehouse);
        return warehouse;
    }

    @Test
    public void mustCreateWarehouse(){
        Warehouse warehouse = this.setupCreate();
        Warehouse warehouseCreated = this.service.create(warehouse);
        assertEquals(warehouseCreated.getId(), warehouse.getId());
    }

    @Test
    public void mustThrowUserNotFoundException(){
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(userRepository.findById(Mockito.any())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, ()-> this.service.create(warehouse));
    }

    @Test
    public void mustThrowUserNotAllowedException(){
        User user = UserFactory.createUserBuyerDefault();
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        assertThrows(UserNotAllowedException.class, ()-> this.service.create(warehouse));
    }

    @Test
    public void mustThrowWarehouseManagerAlreadyDefinedUpdate(){
        User user = UserFactory.createWarehouseManagerDefault();
        Warehouse warehouseToCreate = WarehouseFactory.createWarehouseDefault();
        Warehouse warehouseReturned = WarehouseFactory.createWarehouseDefault();
        warehouseReturned.setWarehouseManager(user);
        warehouseReturned.setId("123");
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(warehouseToCreate));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(repository.findWarehouseByWarehouseManager(Mockito.any())).thenReturn(warehouseReturned);
        assertThrows(WarehouseManagerAlreadyDefined.class, ()-> this.service.update(warehouseToCreate.getId(),warehouseToCreate));
    }

    @Test
    public void mustThrowWarehouseManagerAlreadyDefinedCreate(){
        User user = UserFactory.createWarehouseManagerDefault();
        Warehouse warehouseToCreate = WarehouseFactory.createWarehouseDefault();
        warehouseToCreate.setId(null);
        Warehouse warehouseReturned = WarehouseFactory.createWarehouseDefault();
        warehouseReturned.setWarehouseManager(user);
        warehouseReturned.setId("123");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(repository.findWarehouseByWarehouseManager(Mockito.any())).thenReturn(warehouseReturned);
        assertThrows(WarehouseManagerAlreadyDefined.class, ()-> this.service.create(warehouseToCreate));
    }

    @Test
    public void mustUpdateWarehouse(){
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Warehouse warehouseReturned = WarehouseFactory.createWarehouseDefault();
        User user = UserFactory.createWarehouseManagerDefault();
        Mockito.when(repository.findById(warehouse.getId())).thenReturn(Optional.of(warehouseReturned));
        Mockito.when(repository.save(Mockito.any())).thenReturn(warehouse);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Warehouse warehouseUpdated = this.service.update(warehouse.getId(), warehouse);
        assertEquals(warehouseUpdated.getId(), warehouse.getId());

    }

    @Test
    public void mustGetWarehouseById(){
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(warehouse));
        Warehouse warehouseReturned = this.service.getById(Mockito.any());
        assertEquals(warehouse.getId(), warehouseReturned.getId());
    }

    @Test
    public void mustThrowWarehouseNotFound(){
        Mockito.when(repository.findById(Mockito.any())).thenThrow(WarehouseNotFoundException.class);
        assertThrows(WarehouseNotFoundException.class,()-> this.service.getById(Mockito.any()));
    }

    @Test
    public void mustGetAllWarehouse(){
        Page<Warehouse> page = WarehouseFactory.createPageWarehouse();
        Mockito.when(repository.findAll(Pageable.unpaged())).thenReturn(page);
        Page<Warehouse> pageReturned = this.service.getAll(Pageable.unpaged());
        assertEquals(pageReturned.getTotalElements(), page.getTotalElements());
    }

    @Test
    public void mustDeleteWarehouse(){
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(warehouse));
        this.service.delete(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }


}
