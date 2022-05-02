package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.exception.UserWithThisEmailAlreadyExists;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import br.com.meli.fresh.unit.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BuyerServiceImpTest {

    @Mock
    private IUserRepository repository;

    @Mock
    private IWarehouseRepository warehouseRepository;

    @Mock
    private BCryptPasswordEncoder pe;

    @InjectMocks
    private UserServiceImpl service;

    private User setupBuyer(){
        User buyer = UserFactory.createUserBuyerDefault();
        Mockito.when(repository.save(buyer)).thenReturn(buyer);
        return buyer;
    }

    private Page<User> setupGetAll(){
        Page<User> page = UserFactory.createPageUserBuyers();
        Pageable pageable = Pageable.unpaged();
        Mockito.when(repository.findAll(pageable)).thenReturn(page);
        return page;
    }

    private User setupThrowEmailException(){
        User buyer = UserFactory.createUserBuyerDefault();
        Mockito.when(repository.save(buyer)).thenThrow(UserWithThisEmailAlreadyExists.class);
        return buyer;
    }

    private User setupFindById(){
        User buyer = UserFactory.createUserBuyerDefault();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(java.util.Optional.of(buyer));
        return buyer;
    }

    private void setupThrowNotFoundException(){
        Mockito.when(repository.findById(Mockito.any())).thenThrow(UserNotFoundException.class);
    }

    private User setupUpdate(){
        this.setupFindById();
        return this.setupBuyer();
    }


    @Test
    public void mustCreateBuyer(){
        User buyer = this.setupBuyer();
        User createdBuyer = this.service.create(buyer);
        assertEquals(createdBuyer.getName(), buyer.getName());
    }
    @Test
    public void mustThrowEmailAlreadyExistsException(){
        User buyer = this.setupThrowEmailException();
        assertThrows(UserWithThisEmailAlreadyExists.class,()->{
            this.service.create(buyer);
        });
    }
    @Test
    public void mustGetAllBuyers(){
        Page<User> page = setupGetAll();
        Pageable pageable = Pageable.unpaged();
        Page<User> pageToTest = this.service.getAll(pageable);
        assertEquals(pageToTest.getTotalElements(), page.getTotalElements());

    }
    @Test
    public void mustGetBuyerById(){
        User buyer = this.setupFindById();
        User buyerToTest = this.service.getById(Mockito.any());
        assertEquals(buyerToTest.getName(),buyer.getName());
    }

    @Test
    public void mustThrowBuyerNotFoundException(){
        this.setupThrowNotFoundException();
        assertThrows(UserNotFoundException.class, ()->{
            this.service.getById(Mockito.any());
        });
    }

    @Test
    public void mustUpdateBuyer(){
        User buyer = this.setupUpdate();
        User buyerUpdated = this.service.update(Mockito.any(), buyer);
        assertEquals(buyerUpdated.getName(), buyer.getName());
    }
    @Test
    public void mustDeleteBuyer(){
        this.setupFindById();
        this.service.delete(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }

}
