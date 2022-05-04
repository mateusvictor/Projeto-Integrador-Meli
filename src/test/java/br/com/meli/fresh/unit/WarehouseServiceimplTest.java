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
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceimplTest {

    @Mock
    private static IWarehouseRepository repository;
    @Mock
    private static IUserRepository userRepository;

    @Mock
    private static UserAuthenticatedService authService;

    @InjectMocks
    private WarehouseServiceImpl service;

    private UserSpringSecurity fakeAuthenticatedUSer() {
        // Simulation of a authenticated user by Mockito
        GrantedAuthority admUser = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority warehouseUser = new SimpleGrantedAuthority("ROLE_WAREHOUSEMANAGER");
        GrantedAuthority buyerUser = new SimpleGrantedAuthority("ROLE_BUYER");
        GrantedAuthority sellerUser = new SimpleGrantedAuthority("ROLE_SELLER");
        List<GrantedAuthority> list = List.of(admUser, sellerUser, buyerUser, warehouseUser);
        UserSpringSecurity u = new UserSpringSecurity(
                "1",
                "admin",
                "password",
                Collections.checkedList(list, GrantedAuthority.class)
        );
        Mockito.when(authService.authenticated()).thenReturn(u);
        return u;
    }

    private Warehouse setupCreate(){
        User user = UserFactory.createWarehouseManagerDefault();
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(repository.findByWarehouseManager(Mockito.any())).thenReturn(null);
        Mockito.when(repository.save(Mockito.any())).thenReturn(warehouse);

        this.fakeAuthenticatedUSer();
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
        this.fakeAuthenticatedUSer();
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(userRepository.findById(Mockito.any())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, ()-> this.service.create(warehouse));
    }

    @Test
    public void mustThrowUserNotAllowedException(){
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        assertThrows(UserNotAllowedException.class, ()-> this.service.create(warehouse));
    }

    @Test
    public void mustThrowWarehouseManagerAlreadyDefinedUpdate(){
        this.fakeAuthenticatedUSer();
        User user = UserFactory.createWarehouseManagerDefault();
        Warehouse warehouseToCreate = WarehouseFactory.createWarehouseDefault();
        Warehouse warehouseReturned = WarehouseFactory.createWarehouseDefault();
        warehouseReturned.setWarehouseManager(user);
        warehouseReturned.setId("123");
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(warehouseToCreate));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(repository.findByWarehouseManager(Mockito.any())).thenReturn(warehouseReturned);
        assertThrows(WarehouseManagerAlreadyDefined.class, ()-> this.service.update(warehouseToCreate.getId(),warehouseToCreate));
    }

    @Test
    public void mustThrowWarehouseManagerAlreadyDefinedCreate(){
        this.fakeAuthenticatedUSer();
        User user = UserFactory.createWarehouseManagerDefault();
        Warehouse warehouseToCreate = WarehouseFactory.createWarehouseDefault();
        warehouseToCreate.setId(null);
        Warehouse warehouseReturned = WarehouseFactory.createWarehouseDefault();
        warehouseReturned.setWarehouseManager(user);
        warehouseReturned.setId("123");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(repository.findByWarehouseManager(Mockito.any())).thenReturn(warehouseReturned);
        assertThrows(WarehouseManagerAlreadyDefined.class, ()-> this.service.create(warehouseToCreate));
    }

    @Test
    public void mustUpdateWarehouse(){
        this.fakeAuthenticatedUSer();
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
        this.fakeAuthenticatedUSer();
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(warehouse));
        Warehouse warehouseReturned = this.service.getById("1");
        assertEquals(warehouse.getId(), warehouseReturned.getId());
    }

    @Test
    public void mustThrowWarehouseNotFound(){
        this.fakeAuthenticatedUSer();
        Mockito.when(repository.findById(Mockito.any())).thenThrow(WarehouseNotFoundException.class);
        assertThrows(WarehouseNotFoundException.class,()-> this.service.getById("1"));
    }

    @Test
    public void mustGetAllWarehouse(){
        this.fakeAuthenticatedUSer();
        Page<Warehouse> page = WarehouseFactory.createPageWarehouse();
        Mockito.when(repository.findAll(Pageable.unpaged())).thenReturn(page);
        Page<Warehouse> pageReturned = this.service.getAll(Pageable.unpaged());
        assertEquals(pageReturned.getTotalElements(), page.getTotalElements());
    }

    @Test
    public void mustDeleteWarehouse(){
        this.fakeAuthenticatedUSer();
        Warehouse warehouse = WarehouseFactory.createWarehouseDefault();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(warehouse));
        this.service.delete(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }


}
