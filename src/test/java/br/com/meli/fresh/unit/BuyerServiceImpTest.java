package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import br.com.meli.fresh.repository.IBuyerRepository;
import br.com.meli.fresh.services.impl.BuyerServiceImpl;
import br.com.meli.fresh.unit.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BuyerServiceImpTest {

    @Mock
    private static IBuyerRepository repository;

    @InjectMocks
    private BuyerServiceImpl service;

    private Buyer setupBuyer(){
        Buyer buyer = UserFactory.createBuyerDefault();
        Mockito.when(repository.save(buyer)).thenReturn(buyer);
        return buyer;
    }

    private Page<Buyer> setupGetAll(){
        Page<Buyer> page = UserFactory.createPageBuyer();
        Pageable pageable = Pageable.unpaged();
        Mockito.when(repository.findAll(pageable)).thenReturn(page);
        return page;
    }

    private Buyer setupThrowEmailException(){
        Buyer buyer = UserFactory.createBuyerDefault();
        Mockito.when(repository.save(buyer)).thenThrow(EmailAlreadyExistsException.class);
        return buyer;
    }

    private Buyer setupFindById(){
        Buyer buyer = UserFactory.createBuyerDefault();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(java.util.Optional.of(buyer));
        return buyer;
    }

    private void setupThrowNotFoundException(){
        Mockito.when(repository.findById(Mockito.any())).thenThrow(BuyerNotFoundException.class);
    }

    private Buyer setupUpdate(){
        this.setupFindById();
        return this.setupBuyer();
    }


    @Test
    public void mustCreateBuyer(){
        Buyer buyer = this.setupBuyer();
        Buyer createdBuyer = this.service.create(buyer);
        assertEquals(createdBuyer.getName(), buyer.getName());
    }
    @Test
    public void mustThrowEmailAlreadyExistsException(){
        Buyer buyer = this.setupThrowEmailException();
        assertThrows(EmailAlreadyExistsException.class,()->{
            service.create(buyer);
        });
    }
    @Test
    public void mustGetAllBuyers(){
        Page<Buyer> page = setupGetAll();
        Pageable pageable = Pageable.unpaged();
        Page<Buyer> pageToTest = this.service.getAll(pageable);
        assertEquals(pageToTest.getTotalElements(), page.getTotalElements());

    }
    @Test
    public void mustGetBuyerById(){
        Buyer buyer = this.setupFindById();
        Buyer buyerToTest = this.service.getById(Mockito.any());
        assertEquals(buyerToTest.getName(),buyer.getName());
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
        Buyer buyer = this.setupUpdate();
        Buyer buyerUpdated = this.service.update(Mockito.any(), buyer);
        assertEquals(buyerUpdated.getName(), buyer.getName());
    }
    @Test
    public void mustDeleteBuyer(){
        this.setupFindById();
        this.service.delete(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }

}
