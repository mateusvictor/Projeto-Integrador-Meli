package br.com.meli.fresh.integration;

import br.com.meli.fresh.assembler.SellerMapper;
import br.com.meli.fresh.dto.request.SellerRequestDTO;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.dto.response.SellerResponseDTO;
import br.com.meli.fresh.model.Seller;
import br.com.meli.fresh.services.impl.SellerServiceImpl;
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
@TestPropertySource({"/application-test.properties"})
public class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerServiceImpl service;

    @Autowired
    private SellerMapper mapper;

    @Test
    public void mustGetSellerByID() throws Exception {
        Seller seller = this.service.create(UserFactory.createSellerA());
        SellerResponseDTO responseDTO  = this.mapper.toResponseObject(seller);
        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/seller/{id}", seller.getId()))
                        .andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        SellerResponseDTO responseDTOresult = new ObjectMapper().readValue(jsonReturned, SellerResponseDTO.class);
        assertEquals(responseDTO.getEmail(), responseDTOresult.getEmail());
    }

    @Test
    public void mustThrowNotFoundException() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO("SellerNotFoundException", "Seller not found!");
        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/seller/{id}", "imaginaryID"))
                        .andDo(print()).andExpect(status().isNotFound()).andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDtoResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDtoResult.getError(), errorDTO.getError());
    }

    @Test
    public void mustPostSeller() throws Exception {
        SellerRequestDTO requestDto = UserFactory.createSellerRequestDto();
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(requestDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fresh-products/seller/")
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isCreated()).andReturn();
        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        SellerResponseDTO sellerResponseDTO = new ObjectMapper().readValue(jsonReturned, SellerResponseDTO.class);
        assertEquals(sellerResponseDTO.getEmail(), requestDto.getEmail());
    }

    @Test
    public void mustThrowEmailAlreadyExistsException() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO("EmailAlreadyExistsException", "Email already exists!");
        SellerRequestDTO requestDto = UserFactory.createSellerRequestDtoToThrow();
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(requestDto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fresh-products/seller/")
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isCreated()).andReturn();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fresh-products/seller/")
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isBadRequest()).andReturn();
        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDtoResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDtoResult.getError(), errorDTO.getError());

    }

    @Test
    public void mustUpdateSeller() throws Exception {
        Seller seller = this.service.create(UserFactory.createSellerB());
        SellerRequestDTO requestDto = UserFactory.createSellerToUpdateRequestDto();
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(requestDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/fresh-products/seller/{id}", seller.getId())
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)).andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonReturned =  mvcResult.getResponse().getContentAsString();
        SellerResponseDTO sellerResponseDTO = new ObjectMapper().readValue(jsonReturned, SellerResponseDTO.class);
        assertEquals(sellerResponseDTO.getEmail(), requestDto.getEmail());

    }

    @Test
    public void mustGetAllSellers() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/seller/"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(jsonObjectReturned);
        Integer totalElements = obj.getInt("totalElements");
        System.out.println(totalElements);
        assertEquals(4, totalElements);
    }

    @Test
    public void mustDeleteSeller() throws Exception {
        Seller seller = this.service.create(UserFactory.createSellerC());
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/fresh-products/seller/{id}", seller.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        assertEquals("Seller deleted!", jsonReturned);
    }

}
