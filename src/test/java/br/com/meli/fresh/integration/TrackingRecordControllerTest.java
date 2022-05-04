package br.com.meli.fresh.integration;

import br.com.meli.fresh.assembler.TrackingRecordMapper;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.repository.IPurchaseOrderRepository;
import br.com.meli.fresh.repository.ITrackingRecordRepository;
import br.com.meli.fresh.services.impl.TrackingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Rollback
public class TrackingRecordControllerTest {
    @Autowired
    private TrackingRecordMapper mapper;

    @Autowired
    private TrackingServiceImpl service;

    @Autowired
    private IPurchaseOrderRepository orderRepository;

    @Autowired
    private ITrackingRecordRepository trackingRepository;

    @Autowired
    private AuthFactory auth;

    private final String BASE_URL = "/api/v1/fresh-products/order-tracking";

    @Test
    public void testGetTrackingRecord(){

    }

}
