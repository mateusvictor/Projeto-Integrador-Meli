package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.response.ReportResponse;
import br.com.meli.fresh.dto.response.product.ProductResponse;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.services.impl.ReportServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Sql(scripts = "/report.sql")
@Rollback
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReportServiceImpl service;

    @Autowired
    private AuthFactory auth;

    private final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/reports/";

    @Test
    public void getReport() throws Exception {
        auth.getAdminUser();

        MvcResult mvcResultGet = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "10")
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))).andReturn();

        String jsonObjectReturned = mvcResultGet.getResponse().getContentAsString();
        ReportResponse result = new ObjectMapper().readValue(jsonObjectReturned, ReportResponse.class);

        assertEquals(50, result.getTotalQuantityProducts());
        assertEquals("10", result.getWarehouse());
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
