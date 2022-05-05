package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.response.OrderTotalPriceResponse;
import br.com.meli.fresh.dto.response.SaleResponse;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.factory.BatchFactory;
import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({"/application-test.properties"})
@Rollback
public class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IBatchRepository batchRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IInboundOrderRepository inboundOrderRepository;

    private final String BASE_URL = "/api/v1/fresh-products/sale";



    @Test
    public void testGetAllSaleProduct() throws Exception {
        final Product p = ProductFactory.createProduct();
        Batch b = BatchFactory.createBatch();
        b.setDueDate(LocalDate.now().plusWeeks(1).minusDays(1));
        p.setPrice(BigDecimal.valueOf(10));
        Product pSaved = productRepository.save(p);
        p.setId(pSaved.getId());
        b.setProduct(p);
        batchRepository.save(b);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        SaleResponse saleResponse = new SaleResponse();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        JSONArray obj = new JSONArray(jsonReturned);

        //SaleResponse response = new ObjectMapper().readValue(obj.getString(0), SaleResponse.class);
        List<SaleResponse> result = new ObjectMapper().readValue(jsonReturned, new TypeReference<List<SaleResponse>>() {
        });
        SaleResponse response = result.stream().filter(s -> s.getProductId().equals(p.getId())).findFirst().get();

        assertNotNull(response);
        assertEquals(p.getId(), response.getProductId());
        assertEquals(BigDecimal.valueOf(8.00).setScale(2, RoundingMode.HALF_EVEN), response.getPrice());
    }
}
