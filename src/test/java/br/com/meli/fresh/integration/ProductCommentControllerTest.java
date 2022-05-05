package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.request.AuthRequest;
import br.com.meli.fresh.dto.request.productRequest.ProductCommentRequestDTO;
import br.com.meli.fresh.dto.response.product.ProductCommentResponseDTO;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.factory.UserFactory;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.ProductComment;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.repository.IProductCommentRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.services.impl.ProductCommentServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Rollback
public class ProductCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthFactory auth;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ProductCommentServiceImpl commentService;

    @Autowired
    private IProductCommentRepository commentRepository;

    private final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/products/";


    public String token(User user) {
        // Setting payload login's request
        AuthRequest auth = new AuthRequest();
        auth.setEmail(user.getEmail());
        auth.setPassword("123");

        try {
            ObjectWriter writer = new ObjectMapper()
                    .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                    .writer().withDefaultPrettyPrinter();

            String payloadLogin = writer.writeValueAsString(auth);

            // Realizing auth this user.
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payloadLogin))
                    .andExpect(status().isOk()).andReturn();
            return mvcResult.getResponse().getHeader("Authorization");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Product createProduct(User user) {
        return this.productRepository.save(ProductFactory.getProductComment(user));

    }

    private User createUser(String email){
        return this.userRepository.save(UserFactory.getUserEntityC(email));
    }

    @Test
    public void mustCreateProductComment() throws Exception {
        User userBuyer = createUser("buyercreatecomment0@test.com");
        User userSeller = createUser("sellerproductcomment0@test.com");
        Product product = this.createProduct(userSeller);
        ProductCommentRequestDTO requestDTO = ProductCommentRequestDTO.builder()
                .description("TESTE DE INTEGRACAO NO CREATE TESTE TESTE TESTE TESTE").rating(2).build();
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(requestDTO);
        String url = BASE_URL+product.getId()+"/comment";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                .header(HttpHeaders.AUTHORIZATION, this.token(userBuyer)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        ProductCommentResponseDTO responseDTO = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(jsonObjectReturned,ProductCommentResponseDTO.class);
        assertEquals(requestDTO.getDescription(), responseDTO.getDescription());
    }



    @Test
    public void mustGetById() throws Exception {
        User userBuyer = createUser("buyercreatecomment1@test.com");
        User userSeller = createUser("sellerproductcomment1@test.com");
        Product product = this.createProduct(userSeller);
        ProductComment comment = ProductComment.builder().buyer(userBuyer).product(product)
                .description("teste getbyid teste teste teste teste teste")
                .rating(4).commentDateTime(LocalDateTime.now()).build();
        ProductComment commentCreated = this.commentRepository.save(comment);
        String url = BASE_URL+product.getId()+"/comment/"+commentCreated.getId();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(HttpHeaders.AUTHORIZATION, this.token(userBuyer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        ProductCommentResponseDTO responseDTO = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(jsonObjectReturned,ProductCommentResponseDTO.class);
        assertEquals(commentCreated.getDescription(), responseDTO.getDescription());

    }

    @Test
    public void musGetAll() throws Exception {
        User userBuyer = createUser("buyercreatecomment2@test.com");
        User userSeller = createUser("sellerproductcomment2@test.com");
        Product product = this.createProduct(userSeller);
        ProductComment comment = ProductComment.builder().buyer(userBuyer).product(product)
                .description("teste getbyid teste teste teste teste teste")
                .rating(4).commentDateTime(LocalDateTime.now()).build();
        ProductComment comment2 =ProductComment.builder().buyer(userBuyer).product(product)
                .description("teste getbyid teste teste teste teste teste2")
                .rating(1).commentDateTime(LocalDateTime.now()).build();;
        this.commentRepository.save(comment);
        this.commentRepository.save(comment2);
        String url = BASE_URL+product.getId()+"/comment/";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(HttpHeaders.AUTHORIZATION, this.token(userBuyer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        JSONObject object = new JSONObject(jsonObjectReturned);
        Integer totalElements = object.getInt("totalElements");
        assertEquals(2, totalElements);
    }

    @Test
    public void mustDeleteComment() throws Exception {
        User userBuyer = createUser("buyercreatecomment3@test.com");
        User userSeller = createUser("sellerproductcomment3@test.com");
        Product product = this.createProduct(userSeller);
        ProductComment comment = ProductComment.builder().buyer(userBuyer).product(product)
                .description("teste getbyid teste teste teste teste teste")
                .rating(4).commentDateTime(LocalDateTime.now()).build();
        ProductComment commentCreated = this.commentRepository.save(comment);
        String url = BASE_URL+product.getId()+"/comment/"+commentCreated.getId();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .header(HttpHeaders.AUTHORIZATION, this.token(userBuyer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        assertEquals("Comment deleted!", jsonObjectReturned);
    }
}
