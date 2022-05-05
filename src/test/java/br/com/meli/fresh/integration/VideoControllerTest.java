package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.request.VideoRequest;
import br.com.meli.fresh.dto.request.VideoStatusRequest;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.factory.VideoFactory;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.Video;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IVideoRepository;
import br.com.meli.fresh.services.impl.VideoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({"/application-test.properties"})
@Rollback(value = false)
public class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IVideoRepository videoRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private VideoServiceImpl videoService;

    @Autowired
    private AuthFactory auth;

    private final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/videos/";

    @BeforeEach
    protected void beforeTests() {
        User user = auth.getAdminUser();
        user.setRoles(Set.of(0,1,3));
        userRepository.save(user);
    }

    @AfterEach
    protected void afterTests() {
        videoRepository.deleteAll();
    }



    @Test
    public void testCreateVideoByBuyer() throws Exception {
        Product product = ProductFactory.createProduct();
        Product productCreated = productRepository.save(product);

        VideoRequest request = VideoRequest.builder()
                .title("Any title")
                .url("http://www.youtube.com")
                .length(300)
                .productId(productCreated.getId())
                .build();

        ObjectWriter writer = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequestJson = writer.writeValueAsString(request);

        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                        .andDo(print()).andExpect(status().isCreated()).andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    public void testCreateVideoByBuyerThrowsUserNotAllowedException() throws Exception {
        String token = auth.token(mockMvc);
        // change user permission to seller
        User user = auth.getAdminUser();
        user.setRoles(Set.of(0));
        userRepository.save(user);

        VideoRequest request = VideoRequest.builder()
                .title("Any title")
                .url("http://www.youtube.com")
                .length(300)
                .productId("123")
                .build();

        ObjectWriter writer = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequestJson = writer.writeValueAsString(request);

        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                        .header(HttpHeaders.AUTHORIZATION, token))
                        .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDtoResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals("UserNotAllowedException",errorDtoResult.getError());
    }

    @Test
    public void testCreateVideoByBuyerThrowsProductNotFoundException() throws Exception {
        VideoRequest request = VideoRequest.builder()
                .title("qualquer")
                .url("http://www.youtube.com")
                .length(300)
                .productId("invalid_id")
                .build();

        ObjectWriter writer = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequestJson = writer.writeValueAsString(request);

        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        assertEquals("Product was not found for the given ID! ID:"+request.getProductId(), jsonReturned);
    }


    @Test
    public void testGetAllVideoByProduct() throws Exception {
        String token = auth.token(mockMvc);
        User user = auth.getAdminUser();
        Product product = ProductFactory.createProduct();
        Video video = VideoFactory.createVideo();

        Product productCreated = productRepository.save(product);

        video.setProduct(productCreated);
        video.setUser(user);
        video.setApproved(true);

        videoRepository.save(video);

        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/product/" + productCreated.getId())
                        .header(HttpHeaders.AUTHORIZATION, token))
                        .andDo(print()).andExpect(status().isOk()).andReturn();

        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();

        JSONObject obj = new JSONObject(jsonObjectReturned);
        Integer totalElements = obj.getInt("totalElements");
        assertEquals(1, totalElements);
    }

    @Test
    public void testGetAllVideoBySeller() throws Exception {
        String token = auth.token(mockMvc);
        User user = auth.getAdminUser();
        Product product = ProductFactory.createProduct();
        Video video = VideoFactory.createVideo();

        Product productCreated = productRepository.save(product);

        video.setProduct(productCreated);
        video.setUser(user);
        video.setApproved(true);

        videoRepository.save(video);

        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, token))
                        .andDo(print()).andExpect(status().isOk()).andReturn();

        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(jsonObjectReturned);
        Integer totalElements = obj.getInt("totalElements");
        assertEquals(1, totalElements);
    }

    @Test
    public void testGetAllVideoBySellerThrowsUserNotAllowedException() throws Exception {
        String token = auth.token(mockMvc);
        // change user permission to buyer
        User user = auth.getAdminUser();
        user.setRoles(Set.of(1));
        userRepository.save(user);

        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, token))
                        .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDtoResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals("UserNotAllowedException",errorDtoResult.getError());
    }

    @Test
    public void testChangeApprovalVideo() throws Exception {
        String token = auth.token(mockMvc);
        User user = auth.getAdminUser();
        Product product = ProductFactory.createProduct();
        Video video = VideoFactory.createVideo();
        Product productCreated = productRepository.save(product);

        VideoStatusRequest request = new VideoStatusRequest();
        // change status to true
        request.setStatus(true);

        video.setProduct(productCreated);
        video.setUser(user);
        // save default value to false
        video.setApproved(false);

        ObjectWriter writer = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadRequestJson = writer.writeValueAsString(request);

        Video videoCreated = videoRepository.save(video);

        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + videoCreated.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(payloadRequestJson)
                        .header(HttpHeaders.AUTHORIZATION, token))
                        .andDo(print()).andExpect(status().isOk()).andReturn();

        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        assertEquals("Changed the video status to " + request.getStatus().toString(), jsonObjectReturned);
    }


    @Test
    public void testDeleteVideo() throws Exception {
        String token = auth.token(mockMvc);
        User user = auth.getAdminUser();
        Product product = ProductFactory.createProduct();
        Video video = VideoFactory.createVideo();
        Product productCreated = productRepository.save(product);
        video.setProduct(productCreated);
        video.setUser(user);

        Video videoCreated = videoRepository.save(video);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + videoCreated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print()).andExpect(status().isOk()).andReturn();

    }
}
