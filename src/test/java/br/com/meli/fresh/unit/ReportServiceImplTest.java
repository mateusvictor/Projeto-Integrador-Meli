package br.com.meli.fresh.unit;

import br.com.meli.fresh.dto.response.ReportResponse;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.repository.*;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.impl.ReportServiceImpl;
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock
    private IProductRepository productRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IBatchRepository batchRepository;
    @Mock
    private ISectionRepository sectionRepository;
    @Mock
    private IWarehouseRepository warehouseRepository;
    @Mock
    private static UserAuthenticatedService authService;

    @InjectMocks
    private ReportServiceImpl service;

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

    public List<Batch> reportSetup() {

        // Creating two products
        Product p1 = new Product();
        p1.setCategory("FF");
        Product p2 = new Product();
        p2.setCategory("RF");

        // Creating warehouse
        Warehouse w1 = new Warehouse();
        w1.setId("1");

        // Creating two sections for
        Section s1 = new Section();
        s1.setWarehouse(w1);
        Section s2 = new Section();
        s1.setWarehouse(w1);

        // Vinculating sections in the Warehouse
        w1.setSectionList(List.of(s1, s2));

        // creating inboud order to vinculating batches
        InboundOrder io = new InboundOrder();
        io.setSection(s1);
        io.setSection(s2);

        // Creating two batches for one product
        Batch p1b1 = new Batch();
        Batch p1b2 = new Batch();
        Batch p2b1 = new Batch();
        Batch p2b2 = new Batch();

        // batch 1 from product 1
        p1b1.setCurrentQuantity(10);
        p1b1.setDueDate(LocalDate.of(2022, 5, 20));
        p1b1.setInboundOrder(io);
        p1b1.setProduct(p1);

        // batch 2 from product 1
        p1b2.setCurrentQuantity(5);
        p1b2.setDueDate(LocalDate.of(2022, 5, 10));
        p1b2.setInboundOrder(io);
        p1b2.setProduct(p1);

        // batch 1 from product 2
        p2b1.setCurrentQuantity(15);
        p2b1.setDueDate(LocalDate.of(2022, 5, 7));
        p2b1.setInboundOrder(io);
        p2b1.setProduct(p2);

        // batch 2 from product 2
        p2b2.setCurrentQuantity(20);
        p2b2.setDueDate(LocalDate.of(2022, 5, 25));
        p2b2.setInboundOrder(io);
        p2b2.setProduct(p2);

        List<Batch> list = List.of(p1b1, p1b2, p2b1, p2b2);

        // Simulating the return in userRepository search
        User u = new User();
        u.setId("1");
        u.setRoles(Set.of(0, 1, 2, 3));
        w1.setWarehouseManager(u);
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(u));

        // Simulating the return in warehouseRepository search
        Mockito.when(warehouseRepository.findById("1")).thenReturn(Optional.of(w1));


        // Simulating the return in batch repository search
        Mockito.when(batchRepository.findAllByInboundOrder_Section_Warehouse_Id(Mockito.any())).thenReturn(list);
        return list;
    }

    @Test
    public void testReport() {
        this.reportSetup();
        this.fakeAuthenticatedUSer();

        ReportResponse result = service.getBasicReport("1");

        assertEquals(50, result.getTotalQuantityProducts());
        assertEquals("1", result.getWarehouse());
        assertEquals("1", result.getWarehouseManager());
        assertEquals(4, result.getTotalBatches());
        assertEquals(0, result.getTotalFreshProducts());
        assertEquals(0.0, result.getPercentageOfFreshProducts());
        assertEquals(35, result.getTotalRefrigeratedProducts());
        assertEquals(70.0, result.getPercentageOfRefrigeratedProducts());
        assertEquals(15, result.getTotalFrozenProducts());
        assertEquals(30.0, result.getPercentageOfFrozenProducts());
        assertEquals(2, result.getTotalNextToExpiredProducts());
    }
}
