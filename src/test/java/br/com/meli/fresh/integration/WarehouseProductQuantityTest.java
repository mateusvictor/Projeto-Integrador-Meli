package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.response.ProductQuantityResponse;
import br.com.meli.fresh.factory.*;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.repository.*;
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Rollback
public class WarehouseProductQuantityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InboundOrderServiceImpl service;

    @Autowired
    private IWarehouseRepository warehouseRepository;

    @Autowired
    private ISectionRepository sectionRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IInboundOrderRepository inboundOrderRepository;


    @Autowired
    private IInboundOrderRepository repository;

    @Autowired
    private AuthFactory auth;

    private final String BASE_URL = "/api/v1/fresh-products/warehouse";

    @Test
    public void testGetProductQuantityByWarehouse() throws Exception {
        Product product = ProductFactory.getFreshProductA();
        product = productRepository.save(product);

        Warehouse warehouseA = WarehouseFactory.getWarehouse();
        Section sectionFreshA = SectionFactory.getFreshSection();
        sectionFreshA.setWarehouse(warehouseA);

        Warehouse warehouseB = WarehouseFactory.getWarehouseB();
        Section sectionFreshB = SectionFactory.getFreshSection();
        sectionFreshB.setWarehouse(warehouseB);

        warehouseA.setWarehouseManager(userRepository.save(UserFactory.getUserEntityA()));
        warehouseB.setWarehouseManager(userRepository.save(UserFactory.getUserEntityB()));
        warehouseRepository.save(warehouseA);
        sectionRepository.save(sectionFreshA);
        warehouseRepository.save(warehouseB);
        sectionRepository.save(sectionFreshB);

        InboundOrder inboundOrderA = new InboundOrder(null, null, null, sectionFreshA);
        InboundOrder inboundOrderB = new InboundOrder(null, null, null, sectionFreshB);

        Batch batchA = new Batch("batch1", 10F, 8, 13, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 15F, product, inboundOrderA);
        Batch batchB = new Batch("batch2", 10F, 8, 2, LocalDateTime.parse("2022-08-04T10:11:30"), LocalDate.parse("2022-10-10"), 15F, product, inboundOrderB);

        inboundOrderA.setBatchList(Arrays.asList(batchA));
        inboundOrderB.setBatchList(Arrays.asList(batchB));
        inboundOrderRepository.save(inboundOrderA);
        inboundOrderRepository.save(inboundOrderB);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/productQuantity/" + product.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        ProductQuantityResponse responseDTO = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturned, ProductQuantityResponse.class);

        assertEquals(product.getId(), responseDTO.getProductId());
    }

}
