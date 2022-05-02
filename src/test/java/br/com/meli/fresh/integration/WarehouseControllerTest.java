package br.com.meli.fresh.integration;


import br.com.meli.fresh.assembler.WarehouseMapper;
import br.com.meli.fresh.dto.request.WarehouseRequestDTO;
import br.com.meli.fresh.dto.response.WarehouseResponseDTO;
import br.com.meli.fresh.factory.WarehouseFactory;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import br.com.meli.fresh.services.impl.WarehouseServiceImpl;
import br.com.meli.fresh.unit.factory.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
@Rollback
public class WarehouseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ISectionRepository sectionRepository;

    private final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/warehouse";


    @Test
    public void mustGetWarehouseById() throws Exception {
        Warehouse warehouse = WarehouseFactory.createWarehouse();
        User user = this.userService.create(UserFactory.createWarehouseManagerDefault());

        warehouse.setWarehouseManager(user);
        Warehouse created = this.warehouseService.create(warehouse);


        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", created.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        WarehouseResponseDTO responseDTO = new ObjectMapper().readValue(jsonObjectReturned, WarehouseResponseDTO.class);

        assertEquals(created.getId(), responseDTO.getId());
    }

    @Test
    public void mustGetAllWarehouses() throws Exception {
        Warehouse warehouse = WarehouseFactory.createWarehouse();
        User user = this.userService.create(UserFactory.createWarehouseManagerDefault());
        warehouse.setWarehouseManager(user);
        Warehouse created = this.warehouseService.create(warehouse);

        Warehouse warehouse2 = WarehouseFactory.createWarehouse();
        User user2 = this.userService.create(UserFactory.createWarehouseManagerDefault2());
        warehouse2.setWarehouseManager(user2);
        Warehouse created2 = this.warehouseService.create(warehouse2);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
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
        User user = this.userService.create(UserFactory.createWarehouseManagerDefault());
        WarehouseRequestDTO warehouseRequestDTO = WarehouseFactory.createWarehouseDTO();
        warehouseRequestDTO.setWarehouseManagerId(user.getId());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        WarehouseResponseDTO responseDTO = new ObjectMapper().readValue(jsonObjectReturned, WarehouseResponseDTO.class);

        assertEquals(warehouseRequestDTO.getName(), responseDTO.getName());

    }


}

