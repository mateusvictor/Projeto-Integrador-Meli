package br.com.meli.fresh.integration;

import br.com.meli.fresh.assembler.CartMapper;
import br.com.meli.fresh.dto.request.CartItemRequest;
import br.com.meli.fresh.dto.request.CartRequest;
import br.com.meli.fresh.dto.response.CartResponse;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.dto.response.OrderTotalPriceResponse;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Cart;
import br.com.meli.fresh.model.CartStatus;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.services.impl.CartServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({"/application-test.properties"})
@Sql(scripts = "/setup-tests/create-data-cart.sql")
@Transactional
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final String productId = "3c1f01a6-a59f-42ce-8d19-16fbd40c10ff";
    private final String buyerId = "acb362e2-ebc9-4e06-92ee-790637e28dc3";
    private final int currentQuantity = 10;

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private IBatchRepository batchRepository;

    @Autowired
    private AuthFactory auth;

    @Autowired
    private CartMapper cartMapper;

    public CartRequest setupRequest() {
        CartRequest request = new CartRequest();
        CartItemRequest itemRequest = new CartItemRequest();
        itemRequest.setProductId(productId);
        itemRequest.setQuantity(1);
        request.setBuyerId(buyerId);
        request.setStatus("OPEN");
        request.setItems(Collections.singletonList(itemRequest));

        return request;
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
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isCreated()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        // after
        OrderTotalPriceResponse response = new ObjectMapper().readValue(result, OrderTotalPriceResponse.class);
    }

    @Test
    public void testNotFoundUserBuyer() throws Exception {
        CartRequest request = setupRequest();
        request.setBuyerId("123");

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("UserNotFoundException",errorDTO.getError());
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
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
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
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InsufficientQuantityOfProductException", errorDTO.getError());
    }

    @Test
    public void testDueDateIsNoLessThanThreeWeeksThrowsException() throws Exception {
        CartRequest request = setupRequest();

        Batch batch = batchRepository.findByProduct_Id(productId);
        batch.setDueDate(LocalDate.now());

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequest = writer.writeValueAsString(request);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
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
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InvalidEnumCartStatusException", errorDTO.getError());
    }

    @Test
    public void testCartUpdate() throws Exception {
        CartRequest request = setupRequest();
        Cart cart = cartService.create(cartMapper.toDomainObject(request));
        CartResponse expected = cartMapper.toResponseObject(cart);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isOk()).andReturn();

        Batch batch = batchRepository.findByProduct_Id(productId);

        String result = mvcResult.getResponse().getContentAsString();
        CartResponse response = new ObjectMapper().readValue(result, CartResponse.class);
        assertEquals(expected.getItems().get(0).getProductId(), response.getItems().get(0).getProductId());
        assertEquals(batch.getCurrentQuantity(), currentQuantity - 1);
        assertEquals(CartStatus.CLOSE, CartStatus.valueOf(response.getStatus()));
    }

    @Test
    public void testCartUpdateThrowsCartNotFoundException() throws Exception {
        Cart cart = new Cart();
        cart.setId("123");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isNotFound()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("CartNotFoundException", errorDTO.getError());
    }

    @Test
    public void testCartUpdateThrowsInsufficientQuantityOfProductException() throws Exception {
        CartRequest request = setupRequest();
        Cart cart = cartService.create(cartMapper.toDomainObject(request));

        Batch batch = batchRepository.findByProduct_Id(productId);
        batch.setCurrentQuantity(0);
        batchRepository.save(batch);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InsufficientQuantityOfProductException", errorDTO.getError());
    }

    @Test
    public void testCartUpdateDueDateIsNoLessThanThreeWeeksThrowsException() throws Exception {
        CartRequest request = setupRequest();
        Cart cart = cartService.create(cartMapper.toDomainObject(request));

        Batch batch = batchRepository.findByProduct_Id(productId);
        batch.setDueDate(LocalDate.now());
        batchRepository.save(batch);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isBadRequest()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("InsufficientQuantityOfProductException",errorDTO.getError());
    }


    @Test
    public void testCartGetById() throws Exception {
        CartRequest request = setupRequest();
        Cart cart = cartService.create(cartMapper.toDomainObject(request));
        CartResponse expected = cartMapper.toResponseObject(cart);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        CartResponse response = new ObjectMapper().readValue(result, CartResponse.class);

        assertEquals(expected.getStatus(), response.getStatus());
        assertEquals(expected.getDate(), response.getDate());
        assertEquals(expected.getClass(), response.getClass());
    }

    @Test
    public void testCartGetByIdThrowsCartNotFoundException() throws Exception {
        Cart cart = new Cart();
        cart.setId("123");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + cart.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andExpect(status().isNotFound()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(result, ErrorDTO.class);

        assertEquals("CartNotFoundException", errorDTO.getError());
    }


}
