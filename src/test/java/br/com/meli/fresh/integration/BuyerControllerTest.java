package br.com.meli.fresh.integration;

import br.com.meli.fresh.assembler.UserMapper;
import br.com.meli.fresh.dto.request.UserRequestDTO;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.dto.response.UserResponseDTO;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import br.com.meli.fresh.unit.factory.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
public class BuyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private UserMapper mapper;


    @Test
    public void mustGetBuyerByID() throws Exception {
        User buyer = this.service.create(UserFactory.createUserBuyerA());
        UserResponseDTO responseDTO  = this.mapper.toResponseObject(buyer);
        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/users/{id}", buyer.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        UserResponseDTO responseDTOresult = new ObjectMapper().readValue(jsonReturned, UserResponseDTO.class);
        assertEquals(responseDTO.getEmail(), responseDTOresult.getEmail());
    }

    @Test
    public void mustThrowNotFoundException() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO("UserNotFoundException", "User not found in our database by the id: " + "imaginaryID");
        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/users/{id}", "imaginaryID"))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDtoResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDtoResult.getError(), errorDTO.getError());

    }

    @Test
    public void mustPostBuyer() throws Exception {
        UserRequestDTO requestDto = UserFactory.createBuyerUserRequestDto();
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(requestDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fresh-products/users/")
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isCreated()).andReturn();
        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        UserResponseDTO buyerResponseDTO = new ObjectMapper().readValue(jsonReturned, UserResponseDTO.class);
        assertEquals(buyerResponseDTO.getEmail(), requestDto.getEmail());
    }

    @Test
    public void mustThrowEmailAlreadyExistsException() throws Exception {
        UserRequestDTO requestDto = UserFactory.createBuyerUserRequestDtoToThrow();
        ErrorDTO errorDTO = new ErrorDTO("UserWithThisEmailAlreadyExists", "This " + requestDto.getEmail() + " is already owned by another user!");
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(requestDto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fresh-products/users/")
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isCreated()).andReturn();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fresh-products/users/")
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isBadRequest()).andReturn();;
        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDtoResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDtoResult.getError(), errorDTO.getError());

    }

    @Test
    public void mustUpdateBuyer() throws Exception {
        User buyer = this.service.create(UserFactory.createUserBuyerB());
        UserRequestDTO requestDto = UserFactory.createBuyerUserToUpdateRequestDto();
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(requestDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/fresh-products/users/{id}", buyer.getId())
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        UserResponseDTO buyerResponseDTO = new ObjectMapper().readValue(jsonReturned, UserResponseDTO.class);
        assertEquals(buyerResponseDTO.getEmail(), requestDto.getEmail());

    }

    @Test
    public void mustDeleteBuyer() throws Exception {
        User buyer = this.service.create(UserFactory.createUserBuyerC());
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/fresh-products/users/{id}", buyer.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        assertEquals("User deleted!", jsonReturned);
    }

    @Test
    public void mustGetAllBuyers() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/users/"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(jsonObjectReturned);
        Integer totalElements = obj.getInt("totalElements");
        System.out.println(totalElements);
        assertEquals(4, totalElements);
    }

}
