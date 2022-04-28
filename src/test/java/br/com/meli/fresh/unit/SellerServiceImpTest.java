package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.Seller;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import br.com.meli.fresh.model.exception.SellerNotFoundException;
import br.com.meli.fresh.repository.ISellerRepository;
import br.com.meli.fresh.services.impl.SellerServiceImpl;
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
public class SellerServiceImpTest {

    @Mock
    private static ISellerRepository repository;

    @InjectMocks
    private SellerServiceImpl service;


    private Seller setupSeller() {
        Seller seller = UserFactory.createSeller();
        Mockito.when(repository.save(seller)).thenReturn(seller);
        return seller;
    }

    private Page<Seller> setupGetAll() {
        Page<Seller> page = UserFactory.createPageSeller();
        Pageable pageable = Pageable.unpaged();
        Mockito.when(repository.findAll(pageable)).thenReturn(page);
        return page;
    }

    private Seller setupThrowEmailException() {
        Seller seller = UserFactory.createSeller();
        Mockito.when(repository.save(seller)).thenThrow(EmailAlreadyExistsException.class);
        return seller;
    }

    private Seller setupFindById() {
        Seller seller = UserFactory.createSeller();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(java.util.Optional.of(seller));
        return seller;
    }

    private void setupThrowNotFoundException() {
        Seller seller = UserFactory.createSeller();
        Mockito.when(repository.findById(Mockito.any())).thenThrow(SellerNotFoundException.class);
    }

    private Seller setupUpdate() {
        this.setupFindById();
        return this.setupSeller();
    }

    @Test
    public void mustCreateSeller() {
        Seller seller = this.setupSeller();
        Seller createdSeller = this.service.create(seller);
        assertEquals(createdSeller.getName(), seller.getName());
    }

    @Test
    public void mustThrowEmailAlreadyExistsException() {
        Seller seller = this.setupThrowEmailException();
        assertThrows(EmailAlreadyExistsException.class, () -> {
            service.create(seller);
        });
    }

    @Test
    public void mustGetAllSellers() {
        Page<Seller> page = setupGetAll();
        Pageable pageable = Pageable.unpaged();
        Page<Seller> pageToTest = this.service.getAll(pageable);
        assertEquals(pageToTest.getTotalElements(), page.getTotalElements());
    }

    @Test
    public void mustGetSellerById() {
        Seller seller = this.setupFindById();
        Seller sellerToTest = this.service.getById(Mockito.any());
        assertEquals(sellerToTest.getName(), seller.getName());
    }

    @Test
    public void mustThrowSellerNotFoundException() {
        this.setupThrowNotFoundException();
        assertThrows(SellerNotFoundException.class, () -> {
            this.service.getById(Mockito.any());
        });
    }

    @Test
    public void mustUpdateBuyer() {
        Seller seller = this.setupUpdate();
        Seller sellerUpdate = this.service.update(Mockito.any(), seller);
        assertEquals(sellerUpdate.getName(), seller.getName());
    }

    @Test
    public void mustDeleteSeller() {
        this.setupFindById();
        this.service.delete(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }

}
