package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.request.productRequest.ProductRequest;
import br.com.meli.fresh.dto.response.product.ProductResponse;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Rollback
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductServiceImpl service;

    @Autowired
    private InboundOrderServiceImpl inboundOrderService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthFactory auth;

    private final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/products/";


    @Test
    public void testCreateProduct() throws Exception {

        ProductRequest preq = new ProductRequest();
        preq.setName("Pizza");
        preq.setCategory("RF");
        preq.setMaxTemperature(3.0F);
        preq.setMinTemperature(0.5F);
        preq.setWeight(0.5F);
        BigDecimal price = BigDecimal.valueOf(13.99);
        preq.setPrice(price);

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(preq);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadRequest)
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                        .andDo(print()).andExpect(status().isCreated()).andReturn();

        String locationUrl = mvcResult.getResponse().getHeader("Location");

        assertNotNull(locationUrl);
    }

    @Test
    public void testGetProduct() throws Exception {


        Product p = new Product();
        p.setName("Ice cream");
        p.setCategory("RF");
        Product pSaved = service.create(p);

        MvcResult mvcResultGet = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + pSaved.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))).andReturn();

        String jsonObjectReturned = mvcResultGet.getResponse().getContentAsString();
        ProductResponse pres = new ObjectMapper().readValue(jsonObjectReturned, ProductResponse.class);

        assertEquals("Ice cream", pres.getName());
    }

    @Test
    public void testGetAllProduct() throws Exception {
        Product p1 = new Product();
        Product p2 = new Product();
        p1.setName("Cheese");
        p1.setCategory("RF");
        p2.setName("Ham");
        p2.setCategory("RF");
        service.create(p1);
        service.create(p2);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))).andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(jsonObjectReturned);
        Integer totalElements = obj.getInt("totalElements");
        assertEquals(20, totalElements);
    }

    @Test
    public void gettingProductWithBatchersOrdered() throws Exception {
        Product p =  new Product();
        p.setBatchList(new ArrayList<>());

        Batch b1 = new Batch();
        Batch b2 = new Batch();
        Batch b3 = new Batch();

        // Setting an order by current quantity
        b1.setCurrentQuantity(10);
        b2.setCurrentQuantity(5);
        b3.setCurrentQuantity(1);

        // Setting an order by due date of validation
        b2.setDueDate(LocalDate.of(2022, 5, 4));
        b3.setDueDate(LocalDate.of(2022, 5, 8));
        b1.setDueDate(LocalDate.of(2022, 5, 12));

        p.getBatchList().add(b1);
        p.getBatchList().add(b2);
        p.getBatchList().add(b3);

        Product pSaved = service.create(p);

        MvcResult mvcResultGetCurrentyQuantity = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + pSaved.getId() + "?batch_order=C")
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))).andReturn();

        String jsonObjectReturnedCurrentyQuantity = mvcResultGetCurrentyQuantity.getResponse().getContentAsString();


        ProductResponse pResC = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturnedCurrentyQuantity, ProductResponse.class);

        MvcResult mvcResultGetDueDate = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + pSaved.getId() + "?batch_order=F")
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))).andReturn();

        String jsonObjectReturnedDueDate = mvcResultGetDueDate.getResponse().getContentAsString();
        ProductResponse pResF = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturnedDueDate, ProductResponse.class);

        assertEquals(pResC.getBatchList().get(0).getCurrentQuantity(), 10);
        assertEquals(pResF.getBatchList().get(0).getDueDate(), LocalDate.of(2022, 5, 4));
    }

    @Test
    public void testGetAllProductsFiltered() throws Exception {
        ProductRequest preq = new ProductRequest();
        preq.setName("Sausage");
        preq.setCategory("FF");
        preq.setMaxTemperature(3.0F);
        preq.setMinTemperature(0.5F);
        preq.setWeight(0.5F);
        BigDecimal price = BigDecimal.valueOf(13.99);
        preq.setPrice(price);

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(preq);

        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadRequest)
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isCreated()).andReturn();

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "?category=FF")
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))).andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(jsonObjectReturned);
        Integer totalElements = obj.getInt("totalElements");
        assertEquals(1, totalElements);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product p = new Product();
        p.setName("Lasagna");
        p.setCategory("RF");
        Product pSaved = service.create(p);

        Product pToUpdate = new Product();
        pToUpdate.setName("Popsicle");
        pToUpdate.setCategory("RF");

        MvcResult mvcResultGetWithoutUpdated = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + pSaved.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))).andReturn();
        String objectReturned = mvcResultGetWithoutUpdated.getResponse().getContentAsString();
        ProductResponse pWithoutUpdated = new ObjectMapper().readValue(objectReturned, ProductResponse.class);

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String jsonObjectToBeUpdated = writer.writeValueAsString(pToUpdate);

        this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + pSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObjectToBeUpdated)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        MvcResult MvcResultGetUpdated = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + pSaved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObjectToBeUpdated)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String objectUpdatedReturned = MvcResultGetUpdated.getResponse().getContentAsString();
        ProductResponse pUpdated = new ObjectMapper().readValue(objectUpdatedReturned, ProductResponse.class);

        assertEquals(pWithoutUpdated.getName(), "Lasagna");
        assertEquals(pUpdated.getName(), "Popsicle");
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product p = new Product();
        p.setName("Cheddar");
        p.setCategory("RF");
        Product pSaved = service.create(p);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + pSaved.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isOk()).andReturn();

        MvcResult mvcResultDelete = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + pSaved.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isNotFound()).andReturn();

        String errorMessage = mvcResultDelete.getResponse().getContentAsString();

        assertEquals("Product was not found for the given ID! ID:" + pSaved.getId(), errorMessage);
    }

    @Test
    public void testProductsWereNotFoundException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "?category=FS")
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();
        String errorMessage = mvcResult.getResponse().getContentAsString();
        assertEquals("Products were not found!", errorMessage);
    }
}
