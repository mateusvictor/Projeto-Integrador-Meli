package br.com.meli.fresh.unit;

import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.exception.UserWithThisEmailAlreadyExists;
import br.com.meli.fresh.repository.IUserRepository;
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
public class SellerServiceImpTest {

    @Mock
    private static IUserRepository repository;

    @Mock
    private BCryptPasswordEncoder pe;

    @InjectMocks
    private UserServiceImpl service;


    private User setupUser() {
        User User = UserFactory.createUserSeller();
        Mockito.when(repository.save(User)).thenReturn(User);
        return User;
    }

    private Page<User> setupGetAll() {
        Page<User> page = UserFactory.createPageUserSellers();
        Pageable pageable = Pageable.unpaged();
        Mockito.when(repository.findAll(pageable)).thenReturn(page);
        return page;
    }

    private User setupThrowEmailException() {
        User User = UserFactory.createUserSellerA();
        Mockito.when(repository.save(User)).thenThrow(UserWithThisEmailAlreadyExists.class);
        return User;
    }

    private User setupFindById() {
        User User = UserFactory.createUserSellerC();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(java.util.Optional.of(User));
        return User;
    }

    private void setupThrowNotFoundException() {
        User User = UserFactory.createUserSellerD();
        Mockito.when(repository.findById(Mockito.any())).thenThrow(UserNotFoundException.class);
    }

    private User setupUpdate() {
        this.setupFindById();
        return this.setupUser();
    }

    @Test
    public void mustCreateUser() {
        User User = this.setupUser();
        User createdUser = this.service.create(User);
        assertEquals(createdUser.getName(), User.getName());
    }

    @Test
    public void mustThrowEmailAlreadyExistsException() {
        User User = this.setupThrowEmailException();
        assertThrows(UserWithThisEmailAlreadyExists.class, () -> {
            service.create(User);
        });
    }

    @Test
    public void mustGetAllUsers() {
        Page<User> page = setupGetAll();
        Pageable pageable = Pageable.unpaged();
        Page<User> pageToTest = this.service.getAll(pageable);
        assertEquals(pageToTest.getTotalElements(), page.getTotalElements());
    }

    @Test
    public void mustGetUserById() {
        User User = this.setupFindById();
        User UserToTest = this.service.getById(Mockito.any());
        assertEquals(UserToTest.getName(), User.getName());
    }

    @Test
    public void mustThrowUserNotFoundException() {
        this.setupThrowNotFoundException();
        assertThrows(UserNotFoundException.class, () -> {
            this.service.getById(Mockito.any());
        });
    }

    @Test
    public void mustUpdateBuyer() {
        User User = this.setupUpdate();
        User UserUpdate = this.service.update(Mockito.any(), User);
        assertEquals(UserUpdate.getName(), User.getName());
    }

    @Test
    public void mustDeleteUser() {
        this.setupFindById();
        this.service.delete(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }

}
