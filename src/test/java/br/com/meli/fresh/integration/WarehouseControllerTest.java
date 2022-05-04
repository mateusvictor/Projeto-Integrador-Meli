package br.com.meli.fresh.integration;


import br.com.meli.fresh.dto.request.AuthRequest;
import br.com.meli.fresh.dto.request.WarehouseRequestDTO;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.dto.response.WarehouseResponseDTO;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.factory.WarehouseFactory;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import br.com.meli.fresh.services.impl.WarehouseServiceImpl;
import br.com.meli.fresh.unit.factory.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Rollback
@Disabled
public class WarehouseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthFactory auth;

    private final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/warehouse";
    private String URL_WAREHOUSE;

    public String creatingWarehouse() throws Exception {
        // Creating a warehouse
        WarehouseRequestDTO warehouseRequestDTO = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTO.setWarehouseManagerId(auth.getAdminUser().getId());
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJsonPost = writer.writeValueAsString(warehouseRequestDTO);

        MvcResult mvcResultPost = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJsonPost)
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String url = mvcResultPost.getResponse().getHeader("Location");
        URL_WAREHOUSE = url;
        //
        return url;
    }

    public String token() {
        // Setting payload login's request
        AuthRequest auth = new AuthRequest();
        auth.setEmail("admin@admin.com");
        auth.setPassword("admin");

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

    @AfterEach
    protected void deletingWarehouse() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_WAREHOUSE)
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    public void mustGetWarehouseById() throws Exception {
        User user = this.userService.create(UserFactory.createWarehouseManagerDefault());
        WarehouseRequestDTO warehouseRequestDTO = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTO.setWarehouseManagerId(user.getId());
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(warehouseRequestDTO);

        URL_WAREHOUSE = creatingWarehouse();


        MvcResult mvcResultPost = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                        .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String url = mvcResultPost.getResponse().getHeader("Location");
        URL_WAREHOUSE = url;

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(URL_WAREHOUSE)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        WarehouseResponseDTO responseDTO = new ObjectMapper().readValue(jsonObjectReturned, WarehouseResponseDTO.class);

        assertNotNull(responseDTO);
    }

    @Test
    public void mustGetAllWarehouses() throws Exception {
        Warehouse warehouse = WarehouseFactory.createWarehouse();
        warehouse.setWarehouseManager(auth.getAdminUser());
        auth.login(mockMvc, auth.getAdminUser());
        Warehouse created = this.warehouseService.create(warehouse);

        Warehouse warehouse2 = WarehouseFactory.createWarehouse();
        User user2 = this.userService.create(UserFactory.createWarehouseManagerDefault2());
        warehouse2.setWarehouseManager(user2);
        Warehouse created2 = this.warehouseService.create(warehouse2);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, token()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        JSONObject object = new JSONObject(jsonObjectReturned);
        Integer totalElements = object.getInt("totalElements");
        assertEquals(2, totalElements);
    }

    @Test
    public void mustCreateWarehouse() throws Exception {
        WarehouseRequestDTO warehouseRequestDTO = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTO.setWarehouseManagerId(auth.getAdminUser().getId());
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(warehouseRequestDTO);

        URL_WAREHOUSE = creatingWarehouse();


        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        WarehouseResponseDTO responseDTO = new ObjectMapper().readValue(jsonObjectReturned, WarehouseResponseDTO.class);

        assertEquals(warehouseRequestDTO.getName(), responseDTO.getName());
    }

    @Test
    public void mustUpdateWarehouse() throws Exception {
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        WarehouseRequestDTO warehouseRequestDTOtoUpdate = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTOtoUpdate.setWarehouseManagerId(auth.getAdminUser().getId());

        String payloadRequestJsonPut = writer.writeValueAsString(warehouseRequestDTOtoUpdate);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(URL_WAREHOUSE)
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJsonPut)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        WarehouseResponseDTO responseDTO = new ObjectMapper().readValue(jsonObjectReturned, WarehouseResponseDTO.class);
        assertEquals(responseDTO.getName(),warehouseRequestDTOtoUpdate.getName());
    }

    @Test
    public void mustDeleteWarehouse() throws Exception {
       MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_WAREHOUSE)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonObjectReturned, "Warehouse deleted!");
    }

    @Test
    public void mustThrowWareHouseNotFoundException() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO("WarehouseNotFoundException", "Warehouse not found!");
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/{id}", "imaginaryID")
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTOResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDTOResult.getError(), errorDTO.getError());
    }

    @Test
    public void mustThrowUserNotFoundException() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO("UserNotFoundException", "");
        WarehouseRequestDTO warehouseRequestDTO = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTO.setWarehouseManagerId(auth.getAdminUser().getId());
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(warehouseRequestDTO);

        URL_WAREHOUSE = creatingWarehouse();


        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTOResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDTOResult.getError(), errorDTO.getError());
    }

    @Test
    public void mustThrowUserNotAllowedException() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO("UserNotAllowedException", "UserNotFoundException");
        User user = this.userService.create(UserFactory.createUserBuyerDefault());
        WarehouseRequestDTO warehouseRequestDTO = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTO.setWarehouseManagerId(user.getId());
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(warehouseRequestDTO);

        URL_WAREHOUSE = creatingWarehouse();


        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc, user)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTOResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDTOResult.getError(), errorDTO.getError());
    }

    @Test
    public void mustThrowWarehosueManagerAlreadyDefinedInUpdate() throws Exception {

        ErrorDTO errorDTO = new ErrorDTO("WarehouseManagerAlreadyDefined", "Warehouse manager already defined in another warehouse");
        Warehouse warehouse = WarehouseFactory.createWarehouse();
        warehouse.setWarehouseManager(auth.getAdminUser());
        String strToken = auth.login(mockMvc, auth.getAdminUser());
        Warehouse created = this.warehouseService.create(warehouse);
        Warehouse warehouse2 = WarehouseFactory.createWarehouse();
        User user2 = this.userService.create(UserFactory.createWarehouseManagerDefault2());
        warehouse2.setWarehouseManager(user2);
        this.warehouseService.create(warehouse2);
        WarehouseRequestDTO warehouseRequestDTO = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTO.setWarehouseManagerId(user2.getId());
        ObjectWriter writer  = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();
        String payloadRequestJson = writer.writeValueAsString(warehouseRequestDTO);

        URL_WAREHOUSE = creatingWarehouse();

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(creatingWarehouse())
                .contentType((MediaType.APPLICATION_JSON)).content(payloadRequestJson)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();
        String jsonReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTOResult = new ObjectMapper().readValue(jsonReturned, ErrorDTO.class);
        assertEquals(errorDTOResult.getError(), errorDTO.getError());
    }


}
