package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.request.BatchRequest;
import br.com.meli.fresh.dto.request.InboundOrderRequest;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.dto.response.InboundOrderResponse;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Transactional
public class InboundOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InboundOrderServiceImpl service;

    @Autowired
    private IWarehouseRepository warehouseRepository;

    @Autowired
    private ISectionRepository sectionRepository;

    @Autowired
    private IProductRepository productRepository;

    private final String BASE_URL = "/api/v1/fresh-products/inboundorder";

    // Used to make POST and
    private final ObjectWriter writer = new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .writer().withDefaultPrettyPrinter();

    public InboundOrderRequest getValidRequestInstance(){
        Warehouse warehouse = new Warehouse(null, "SP - WAREHOUSE", null, null);
        warehouse = warehouseRepository.save(warehouse);

        Section sectionFresh = new Section(null, "fresco", 0F, 30F, null, warehouse);
        sectionFresh = sectionRepository.save(sectionFresh);

        Product product1 = new Product(null, "Bolacha Trakinas", "fresco", 5F, 30F, 0.25F, null, null, true);
        product1 = productRepository.save(product1);

        Product product2 = new Product(null, "Batata Doce", "fresco", 10F, 30F, 1F, null, null, true);
        product2 = productRepository.save(product2);

        List<BatchRequest> batchList = Arrays.asList(
                new BatchRequest(product1.getId(), 10F, 8, 8, null, null, 10F),
                new BatchRequest(product2.getId(), 10F, 5, 5, null, null, 12F)
        );
        return new InboundOrderRequest(null, sectionFresh.getId(), batchList);
    }


    public InboundOrderRequest getInstanceWithInvalidProductType(){
        // Returns an instance with a product that doesn't match the section product type
        // Section product type: fresco
        // Product type: congelado
        Warehouse warehouse = new Warehouse(null, "SP - WAREHOUSE", null, null);
        warehouse = warehouseRepository.save(warehouse);

        Section sectionFresh = new Section(null, "fresco", 0F, 30F, null, warehouse);
        sectionFresh = sectionRepository.save(sectionFresh);

        // Invalid product
        Product product1 = new Product(null, "Pizza Quatro Queijos", "congelado", 5F, 30F, 0.25F, null, null, true);
        product1 = productRepository.save(product1);

        Product product2 = new Product(null, "Batata Doce", "fresco", 10F, 30F, 1F, null, null, true);
        product2 = productRepository.save(product2);

        List<BatchRequest> batchList = Arrays.asList(
                new BatchRequest(product1.getId(), 10F, 8, 8, null, null, 10F),
                new BatchRequest(product2.getId(), 10F, 5, 5, null, null, 12F)
        );
        return new InboundOrderRequest(null, sectionFresh.getId(), batchList);
    }

    public InboundOrderRequest getInstanceWithInvalidVolume(){
        // Returns an instance a batch list with more volume than the section available volume
        // Section available volume: 20.0
        // Batch total volume: 30.0
        Warehouse warehouse = new Warehouse(null, "SP - WAREHOUSE", null, null);
        warehouse = warehouseRepository.save(warehouse);

        Section section = new Section(null, "congelado", 10F, 30F, null, warehouse);
        section= sectionRepository.save(section);

        Product product1 = new Product(null, "Pizza Quatro Queijos", "congelado", -10F, 30F, 1F, null, null, true);
        product1 = productRepository.save(product1);

        Product product2 = new Product(null, "Pao de Queijo", "congelado", -30F, 30F, 1F, null, null, true);
        product2 = productRepository.save(product2);

        // Batch total volume: 17.0 + 13.0 = 30.0
        List<BatchRequest> batchList = Arrays.asList(
                new BatchRequest(product1.getId(), 10F, 8, 8, null, null, 17F),
                new BatchRequest(product2.getId(), 10F, 5, 5, null, null, 13F)
        );
        return new InboundOrderRequest(null, section.getId(), batchList);
    }

    public InboundOrder getInboundOrderEntity(){
        Warehouse warehouse = new Warehouse(null, "SP - WAREHOUSE", null, null);
        warehouse = warehouseRepository.save(warehouse);

        Section sectionFresh = new Section(null, "fresco", 0F, 30F, null, warehouse);
        sectionFresh = sectionRepository.save(sectionFresh);
        InboundOrder inboundOrder = new InboundOrder(null, null, null, sectionFresh);

        Product product1 = new Product(null, "Bolacha Trakinas", "fresco", 5F, 30F, 0.25F, null, null, true);
        product1 = productRepository.save(product1);

        Product product2 = new Product(null, "Batata Doce", "fresco", 10F, 30F, 1F, null, null, true);
        product2 = productRepository.save(product2);

        List<Batch> batchList = Arrays.asList(
                new Batch(null, 10F, 8, 8, null, null, 10F, product1, null),
                new Batch(null, 10F, 5, 5, null, null, 10F, product2, null)
        );

        inboundOrder.setBatchList(batchList);

        return inboundOrder;
    }

    @Test
    public void testCreateInboundOrder() throws Exception {
        InboundOrderRequest inboundOrder = this.getValidRequestInstance();

        String payloadRequest = writer.writeValueAsString(inboundOrder);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String locationUrl = mvcResult.getResponse().getHeader("Location");
        assertNotNull(locationUrl);
    }

    @Test
    public void testGetInboundOrder() throws Exception {
        // First creates an inbound order
        InboundOrder inboundOrder = service.create(this.getInboundOrderEntity());

        // Perform the GET with the inboundOrder ID
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + inboundOrder.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        InboundOrderResponse responseDTO = new ObjectMapper().readValue(jsonObjectReturned, InboundOrderResponse.class);

        assertEquals(inboundOrder.getId(), responseDTO.getId());
        assertEquals(inboundOrder.getSection().getId(), responseDTO.getSection().getId());
    }

    @Test
    public void testUpdateInboundOrder() throws Exception {
        // First creates an inbound order
        InboundOrder oldInboundOrder = service.create(this.getInboundOrderEntity());
        InboundOrderRequest inboundOrderRequest = new InboundOrderRequest();
        Float oldSectionVolume = oldInboundOrder.getSection().getActualVolume();

        // Updating the batch list to only one product
        List<BatchRequest> newBatchList = Arrays.asList(
                new BatchRequest(oldInboundOrder.getBatchList().get(0).getProduct().getId(), 1F, 78, 8, null, null, 9F)
        );
        inboundOrderRequest.setOrderDateTime(oldInboundOrder.getOrderDateTime());
        inboundOrderRequest.setSectionId(oldInboundOrder.getSection().getId());
        inboundOrderRequest.setBatchStock(newBatchList);

        String payloadRequest = writer.writeValueAsString(inboundOrderRequest);

        // Perform the PUT request
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + oldInboundOrder.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        InboundOrderResponse inboundOrderResponse = new ObjectMapper().readValue(jsonObjectReturned, InboundOrderResponse.class);

        // The section volume should be updated
        assertNotEquals(oldSectionVolume, inboundOrderResponse.getSection().getActualVolume());
    }

    @Test
    public void testInvalidSectionTypeException() throws Exception {
        InboundOrderRequest inboundOrder = this.getInstanceWithInvalidProductType();

        String payloadRequest = writer.writeValueAsString(inboundOrder);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        List<ErrorDTO> errorDTO = new ObjectMapper().readValue(jsonObjectReturned, new TypeReference<List<ErrorDTO>>() {
        });

        assertEquals(2, errorDTO.size());
    }

    @Test
    public void testMethodArgumentNotValidException() throws Exception {
        InboundOrderRequest inboundOrder = this.getValidRequestInstance();

        // Setting section ID to null and batch list to empty to throw MethodArgumentNotValidException
        inboundOrder.setSectionId(null);
        inboundOrder.setBatchStock(null);

        String payloadRequest = writer.writeValueAsString(inboundOrder);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        List<ErrorDTO> errorList = new ObjectMapper().readValue(jsonObjectReturned, new TypeReference<List<ErrorDTO>>() {});

        assertEquals(2, errorList.size());
        errorList.forEach(
                e -> assertEquals("MethodArgumentNotValidException", e.getError())
        );
    }

    @Test
    public void testInsufficientAvailableSpaceException() throws Exception {
        InboundOrderRequest inboundOrder = this.getInstanceWithInvalidVolume();

        String payloadRequest = writer.writeValueAsString(inboundOrder);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        List<ErrorDTO> errorDTO = new ObjectMapper().readValue(jsonObjectReturned, new TypeReference<List<ErrorDTO>>() {
        });

        assertEquals(2, errorDTO.size());
    }

    @Test
    public void testInboundOrderNotFound() throws Exception {
        // Perform the GET with a invalid ID
        String invalidId = "INVALID_ID";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + invalidId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(jsonObjectReturned, ErrorDTO.class);

        assertEquals(errorDTO.getError(), "EntityNotFoundException");
        assertEquals(errorDTO.getDescription(), "Invalid inbound order ID: " + invalidId);
    }
}
