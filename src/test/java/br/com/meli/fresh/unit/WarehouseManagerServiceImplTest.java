package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.WarehouseManager;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import br.com.meli.fresh.repository.IBuyerRepository;
import br.com.meli.fresh.repository.IWarehouseManagerRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.impl.BuyerServiceImpl;
import br.com.meli.fresh.services.impl.WarehouseManagerServiceImpl;
import br.com.meli.fresh.unit.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WarehouseManagerServiceImplTest {

    @Mock
    private static IWarehouseManagerRepository repository;

    @Mock
    private static IWarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseManagerServiceImpl service;

    private WarehouseManager setupWarehouseManager(){
        WarehouseManager warehouseManager = UserFactory.createWarehouseManagerDefault();
        Mockito.when(repository.save(warehouseManager)).thenReturn(warehouseManager);
        return warehouseManager;
    }

    private Page<WarehouseManager> setupGetAll(){
        Page<WarehouseManager> page = UserFactory.createWarehouseManagerPage();
        Pageable pageable = Pageable.unpaged();
        Mockito.when(repository.findAll(pageable)).thenReturn(page);
        
        return page;
    }

    private WarehouseManager setupThrowEmailException(){
        WarehouseManager warehouseManager = UserFactory.createWarehouseManagerDefault();
        Mockito.when(repository.save(warehouseManager)).thenThrow(EmailAlreadyExistsException.class);
        return warehouseManager;
    }

    private WarehouseManager setupFindById(){
        WarehouseManager warehouseManager = UserFactory.createWarehouseManagerDefault();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(java.util.Optional.of(warehouseManager));
        return warehouseManager;
    }

    private void setupThrowNotFoundException(){
        Mockito.when(repository.findById(Mockito.any())).thenThrow(BuyerNotFoundException.class);
    }

    private WarehouseManager setupUpdate(){
        this.setupFindById();
        return this.setupWarehouseManager();
    }


    @Test
    public void mustCreateBuyer(){
        WarehouseManager warehouseManager = this.setupWarehouseManager();
        WarehouseManager warehouseCreated = this.service.create(warehouseManager);
        assertEquals(warehouseCreated.getName(), warehouseManager.getName());
    }
    @Test
    public void mustThrowEmailAlreadyExistsException(){
        WarehouseManager warehouseManager = this.setupThrowEmailException();
        assertThrows(EmailAlreadyExistsException.class,()->{
            service.create(warehouseManager);
        });
    }
    @Test
    public void mustGetAllWarehouseManager(){
        Page<WarehouseManager> page = setupGetAll();
        Pageable pageable = Pageable.unpaged();
        Page<WarehouseManager> pageToTest = this.service.getAll(pageable);
        assertEquals(pageToTest.getTotalElements(), page.getTotalElements());

    }
    @Test
    public void mustGetBuyerById(){
        WarehouseManager warehouseManager = this.setupFindById();
        WarehouseManager warehouseManagerToTest = this.service.getById(Mockito.any());
        assertEquals(warehouseManagerToTest.getName(),warehouseManager.getName());
    }

    @Test
    public void mustThrowBuyerNotFoundException(){
        this.setupThrowNotFoundException();
        assertThrows(BuyerNotFoundException.class, ()->{
            this.service.getById(Mockito.any());
        });
    }

    @Test
    public void mustUpdateBuyer(){
        WarehouseManager warehouseManager = this.setupUpdate();
        WarehouseManager warehouseManagerUpdated = this.service.update(Mockito.any(), warehouseManager);
        assertEquals(warehouseManagerUpdated.getName(), warehouseManager.getName());
    }
    @Test
    public void mustDeleteBuyer(){
        this.setupFindById();
        this.service.delete(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }

}
