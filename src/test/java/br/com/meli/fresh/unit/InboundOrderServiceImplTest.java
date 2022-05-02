package br.com.meli.fresh.unit;

import br.com.meli.fresh.factory.InboundOrderFactory;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IInboundOrderRepository;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import br.com.meli.fresh.services.exception.InsufficientAvailableSpaceException;
import br.com.meli.fresh.services.exception.InvalidSectionTypeException;
import br.com.meli.fresh.services.exception.InvalidWarehouseManagerException;
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceImplTest {
    @Mock
    private static IInboundOrderRepository orderRepository;
    @Mock
    private static IBatchRepository batchRepository;
    @Mock
    private static ISectionRepository sectionRepository;
    @Mock
    private static UserAuthenticatedService authService;

    @InjectMocks
    private InboundOrderServiceImpl orderService;

    public User getUserEntity(){
        User user = new User(
                "user1",
                "admin",
                "admin@gmail.com",
                "1234",
                Set.of(0, 1, 2, 3));

        return user;
    }

    public UserSpringSecurity getUser(){
        User user = getUserEntity();
        Collection<GrantedAuthority> grantedAuthorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        UserSpringSecurity userSS = new UserSpringSecurity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
        return userSS;
    }

    @Test
    public void testInboundOrderCreation(){
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();
        inboundOrder.getSection().getWarehouse().setWarehouseManager(this.getUserEntity());

        Mockito.when(orderRepository.save(inboundOrder)).thenReturn(inboundOrder);
        Mockito.when(authService.authenticated()).thenReturn(this.getUser());

        inboundOrder = orderService.create(inboundOrder);
        assertNotNull(inboundOrder);
    }

    @Test
    public void testGetInboundOrder(){
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();
        Mockito.when(orderRepository.findById("order1")).thenReturn(Optional.of(inboundOrder));

        inboundOrder = orderService.getById("order1");
        assertNotNull(inboundOrder);
    }

    @Test
    public void testUpdateInboundOrder(){
        // First creates an inbound order
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();
        inboundOrder.getSection().getWarehouse().setWarehouseManager(this.getUserEntity());

        Mockito.when(orderRepository.save(inboundOrder)).thenReturn(inboundOrder);
        Mockito.when(authService.authenticated()).thenReturn(this.getUser());

        inboundOrder = orderService.create(inboundOrder);
        Double oldTotalVolume = inboundOrder.calculateBatchesTotalVolume();

        // Mocks the method called in the update method
        Mockito.when(orderRepository.findById(inboundOrder.getId())).thenReturn(Optional.of(inboundOrder));

        // Updates the volume of one batch
        InboundOrder inboundOrderUpdated = inboundOrder;

        List<Batch> batchListUpdated = inboundOrderUpdated.getBatchList();
        batchListUpdated.get(0).setVolume(5F);
        inboundOrderUpdated.setBatchList(batchListUpdated);

        inboundOrderUpdated = orderService.update(inboundOrder.getId(), inboundOrderUpdated);

        assertEquals(inboundOrder.getId(), inboundOrderUpdated.getId());
        assertNotEquals(oldTotalVolume, inboundOrder.calculateBatchesTotalVolume());
    }

    @Test
    public void testInboundOrderNotFoundException(){
        Mockito.when(orderRepository.findById("invalidId")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.getById("invalidId");
        });
    }

    @Test
    public void testInvalidSectionTypeException(){
        InboundOrder invalidInboundOrder = InboundOrderFactory.getInstanceWithInvalidProductType();
        invalidInboundOrder.getSection().getWarehouse().setWarehouseManager(this.getUserEntity());

        Mockito.when(authService.authenticated()).thenReturn(this.getUser());

        assertThrows(InvalidSectionTypeException.class, () -> {
            orderService.create(invalidInboundOrder);
        });
    }

    @Test
    public void testInsufficientAvailableSpaceException(){
        InboundOrder invalidInboundOrder = InboundOrderFactory.getInstanceWithInvalidVolume();
        invalidInboundOrder.getSection().getWarehouse().setWarehouseManager(this.getUserEntity());

        Mockito.when(authService.authenticated()).thenReturn(this.getUser());

        assertThrows(InsufficientAvailableSpaceException.class, () -> {
            orderService.create(invalidInboundOrder);
        });
    }

    @Test
    public void testInvalidWarehouseManager(){
        InboundOrder inboundOrder = InboundOrderFactory.getValidInstance();
        User userManager = this.getUserEntity();
        userManager.setId("userManagerId");
        inboundOrder.getSection().getWarehouse().setWarehouseManager(userManager);

        Mockito.when(authService.authenticated()).thenReturn(this.getUser()); // The ID of the manager doesn't match the client id

        assertThrows(InvalidWarehouseManagerException.class, () -> {
            orderService.create(inboundOrder);
        });
    }
}
