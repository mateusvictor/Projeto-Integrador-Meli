package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.request.CartItemRequest;
import br.com.meli.fresh.dto.request.CartRequest;
import br.com.meli.fresh.dto.response.CartResponse;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.dto.response.OrderTotalPriceResponse;
import br.com.meli.fresh.factory.*;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.repository.*;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({"/application-test.properties"})
@Rollback
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private String productId;
    private final int currentQuantity = 10;

    @Autowired
    private IBatchRepository batchRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IInboundOrderRepository inboundOrderRepository;

    @Autowired
    private ISectionRepository sectionRepository;

    @Autowired
    private IWarehouseRepository warehouseRepository;

    @Autowired
    private UserServiceImpl userService;


    public CartRequest setupRequest() {
        CartRequest request = new CartRequest();
        CartItemRequest itemRequest = new CartItemRequest();
        itemRequest.setProductId(productId);
        itemRequest.setQuantity(1);
        request.setStatus("OPEN");
        request.setItems(Collections.singletonList(itemRequest));

        return request;
    }


    public void setupData() {
        Warehouse warehouse = WarehouseFactory.getWarehouse();
        warehouseRepository.save(warehouse);

        Section section = SectionFactory.createSection();
        Section sectionSaved = sectionRepository.save(section);

        InboundOrder inboundOrder = new InboundOrder();
        inboundOrder.setOrderDateTime(LocalDateTime.now());
        inboundOrder.setSection(sectionSaved);

        InboundOrder inboundOrderSaved = inboundOrderRepository.save(inboundOrder);

        Product product = ProductFactory.createProduct();
        Batch batch = BatchFactory.createBatch(product, inboundOrderSaved);
        batch.setCurrentQuantity(currentQuantity);
        product.setBatchList(List.of(batch));

        Product productSaved = productRepository.save(product);
        this.productId = productSaved.getId();
        batchRepository.save(batch);
    }

    @BeforeEach
    protected void beforeTest() {
        User user = BuyerFactory.createBuyer();
        AuthSingle.getInstance(userService, mockMvc, user);

        setupData();
    }

    private final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/orders/";

    @Test
    public void testCreateCart() throws Exception {
        CartRequest request = setupRequest();

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andDo(print()).andExpect(status().isCreated()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        OrderTotalPriceResponse response = new ObjectMapper().readValue(result, OrderTotalPriceResponse.class);
    }


    @Test
    public void testNotFoundProduct() throws Exception {

        CartRequest request = setupRequest();
        CartItemRequest cartRequest = new CartItemRequest();
        cartRequest.setProductId("123");
        cartRequest.setQuantity(1);
        request.setItems(Collections.singletonList(cartRequest));

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("Product was not found for the given ID! ID:123", result);
    }

    @Test
    public void testInsufficientQuantityOfProductException() throws Exception {

        CartRequest request = setupRequest();
        CartItemRequest cartRequest = new CartItemRequest();
        cartRequest.setProductId(productId);
        cartRequest.setQuantity(currentQuantity + 1);
        request.setItems(Collections.singletonList(cartRequest));

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InsufficientQuantityOfProductException", errorDTO.getError());
    }

    @Test
    public void testDueDateIsGreaterThanThreeWeeksThrowsException() throws Exception {

        CartRequest request = setupRequest();

        Batch batch = batchRepository.findByProduct_Id(productId);
        batch.setDueDate(LocalDate.now());

        batchRepository.save(batch);

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InsufficientQuantityOfProductException", errorDTO.getError());
    }

    @Test
    public void testInvalidEnumCartStatusException() throws Exception {

        CartRequest request = setupRequest();
        request.setStatus("ANY");

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InvalidEnumCartStatusException", errorDTO.getError());
    }

    @Test
    public void testCartUpdate() throws Exception {
        CartRequest request = setupRequest();

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);

        MvcResult mvcResultPost = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andExpect(status().isCreated()).andReturn();

        String url = mvcResultPost.getResponse().getHeader("Location");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andExpect(status().isOk()).andReturn();

        Batch batch = batchRepository.findByProduct_Id(productId);

        String result = mvcResult.getResponse().getContentAsString();
        CartResponse response = new ObjectMapper().readValue(result, CartResponse.class);
//        assertEquals(expected.getItems().get(0).getProductId(), response.getItems().get(0).getProductId());
        assertEquals(batch.getCurrentQuantity(), currentQuantity - 1);
        assertEquals(CartStatus.CLOSE, CartStatus.valueOf(response.getStatus()));
    }

    @Test
    public void testCartUpdateThrowsCartNotFoundException() throws Exception {

        Cart cart = new Cart();
        cart.setId("123");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andExpect(status().isNotFound()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("CartNotFoundException", errorDTO.getError());
    }

    @Test
    public void testCartUpdateThrowsInsufficientQuantityOfProductException() throws Exception {

        CartRequest request = setupRequest();
        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);

        MvcResult mvcResultPost = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andExpect(status().isCreated()).andReturn();

        String url = mvcResultPost.getResponse().getHeader("Location");

        Batch batch = batchRepository.findByProduct_Id(productId);
        batch.setCurrentQuantity(0);
        batchRepository.save(batch);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InsufficientQuantityOfProductException", errorDTO.getError());
    }

    @Test
    public void testCartUpdateDueDateIsGreaterThanThreeWeeksThrowsException() throws Exception {

        CartRequest request = setupRequest();
        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);

        MvcResult mvcResultPost = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andExpect(status().isCreated()).andReturn();

        String url = mvcResultPost.getResponse().getHeader("Location");

        Batch batch = batchRepository.findByProduct_Id(productId);
        batch.setDueDate(LocalDate.now());
        batchRepository.save(batch);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InsufficientQuantityOfProductException", errorDTO.getError());
    }


    @Test
    public void testCartGetById() throws Exception {

        CartRequest request = setupRequest();
        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);

        MvcResult mvcResultPost = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andExpect(status().isCreated()).andReturn();

        String url = mvcResultPost.getResponse().getHeader("Location");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        CartResponse response = new ObjectMapper().readValue(result, CartResponse.class);

        assertEquals(request.getStatus(), response.getStatus());
        // todo
    }

    @Test
    public void testCartGetByIdThrowsCartNotFoundException() throws Exception {

        Cart cart = new Cart();
        cart.setId("123");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, AuthSingle.TOKEN))
                .andExpect(status().isNotFound()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("CartNotFoundException", errorDTO.getError());
    }


}
