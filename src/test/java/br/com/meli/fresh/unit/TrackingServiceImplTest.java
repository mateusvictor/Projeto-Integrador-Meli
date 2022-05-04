package br.com.meli.fresh.unit;


import br.com.meli.fresh.model.OrderStatus;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.model.TrackingRecord;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.repository.IPurchaseOrderRepository;
import br.com.meli.fresh.repository.ITrackingRecordRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.impl.TrackingServiceImpl;
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TrackingServiceImplTest {
    @Mock
    private ITrackingRecordRepository trackingRepository;
    @Mock
    private IPurchaseOrderRepository purchaseOrderRepository;
    @Mock
    private UserAuthenticatedService auth;

    @InjectMocks
    private TrackingServiceImpl service;

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
    public void testCreateTrackingRecord(){
        PurchaseOrder order = new PurchaseOrder(null, LocalDateTime.now(), null, null);
        TrackingRecord trackingRecord = new TrackingRecord(null, LocalDateTime.now(), "SAO PAULO", "Seu pedido saiu para entrega", OrderStatus.ON_THE_WAY, order);
        Mockito.when(trackingRepository.save(trackingRecord)).thenReturn(trackingRecord);
        Mockito.when(auth.authenticated()).thenReturn(this.getUser());

        trackingRecord = service.create(trackingRecord);
        assertNotNull(trackingRecord);
    }

    @Test
    public void testGetTrackingRecordList(){
        PurchaseOrder order = new PurchaseOrder("123", LocalDateTime.now(), null, null);
        TrackingRecord trackingRecord1 = new TrackingRecord(null, LocalDateTime.now(), "SAO PAULO", "Seu pedido saiu para entrega", OrderStatus.ON_THE_WAY, order);
        TrackingRecord trackingRecord2 = new TrackingRecord(null, LocalDateTime.now(), "SAO PAULO - TATUAPE", "Seu pedido esta a caminho", OrderStatus.ON_THE_WAY, order);

        Mockito.when(trackingRepository.findByPurchaseOrder(order)).thenReturn(Arrays.asList(trackingRecord1, trackingRecord2));
        Mockito.when(purchaseOrderRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(order));

        List<TrackingRecord> trackingRecordList = service.getTrackingRecordList(order.getId());

        assertNotNull(trackingRecordList);
        assertEquals(trackingRecord2, trackingRecordList.stream().findFirst().orElse(null));
    }

    @Test
    public void testGetTrackingRecordById(){
        PurchaseOrder order = new PurchaseOrder("123", LocalDateTime.now(), null, null);
        TrackingRecord trackingRecord = new TrackingRecord("123", LocalDateTime.now(), "SAO PAULO", "Seu pedido saiu para entrega", OrderStatus.ON_THE_WAY, order);
        Mockito.when(trackingRepository.findById(trackingRecord.getId())).thenReturn(Optional.of(trackingRecord));
        trackingRecord = service.getById(trackingRecord.getId());
        assertNotNull(trackingRecord);
    }

    @Test
    public void updateTrackingRecord(){
        PurchaseOrder order = new PurchaseOrder("123", LocalDateTime.now(), null, null);
        TrackingRecord trackingRecord = new TrackingRecord("123", LocalDateTime.now(), "SAO PAULO", "Seu pedido saiu para entrega", OrderStatus.ON_THE_WAY, order);
        TrackingRecord trackingRecordUpdated = new TrackingRecord("123", LocalDateTime.now(), "SAO PAULO", "Seu pedido esta aguardando o caminhao de entrega", OrderStatus.ON_THE_WAY, order);

        Mockito.when(auth.authenticated()).thenReturn(this.getUser());
        Mockito.when(trackingRepository.save(Mockito.any())).thenReturn(trackingRecordUpdated);
        Mockito.when(trackingRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(trackingRecord));

        trackingRecord = service.update(trackingRecord.getId(), trackingRecordUpdated);

        assertNotNull(trackingRecord);
        assertEquals(trackingRecordUpdated.getDetailMessage(), trackingRecord.getDetailMessage());
    }


}
