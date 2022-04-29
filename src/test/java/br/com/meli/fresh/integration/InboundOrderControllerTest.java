package br.com.meli.fresh.integration;

import br.com.meli.fresh.dto.request.BatchRequest;
import br.com.meli.fresh.dto.request.InboundOrderRequest;
import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.dto.response.InboundOrderResponse;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.factory.SectionFactory;
import br.com.meli.fresh.factory.WarehouseFactory;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.repository.IInboundOrderRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Rollback
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

    @Autowired
    private IInboundOrderRepository repository;

    @Autowired
    private AuthFactory auth;

    private final String BASE_URL = "/api/v1/fresh-products/inboundorder";

    // Used to make POST and
    private final ObjectWriter writer = new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .registerModule(new JavaTimeModule())
            .writer().withDefaultPrettyPrinter();

    public InboundOrderRequest getValidRequestInstance(){
        Warehouse warehouse = WarehouseFactory.getWarehouse();
        Section sectionFresh = SectionFactory.getFreshSection();
        sectionFresh.setWarehouse(warehouse);

        warehouse.setSectionList(Collections.singletonList(sectionFresh));
        warehouse.setWarehouseManager(auth.getAdminUser());

        warehouse = warehouseRepository.save(warehouse);

        Product product1 = productRepository.save(ProductFactory.getFreshProductA());
        Product product2 = productRepository.save(ProductFactory.getFreshProductB());

        List<BatchRequest> batchList = Arrays.asList(
                new BatchRequest(product1.getId(), 10F, 8, 8, null, LocalDate.now(), 10F),
                new BatchRequest(product2.getId(), 10F, 5, 5, null, LocalDate.now(), 12F)
        );
        return new InboundOrderRequest(null, sectionFresh.getId(), batchList);
    }

    public InboundOrderRequest getInstanceWithInvalidProductType(){
        // Returns an instance with a product that doesn't match the section product type
        // Section product type: fresco
        // Product type: congelado
        Warehouse warehouse = WarehouseFactory.getWarehouse();
        Section sectionFresh = SectionFactory.getFreshSection();
        sectionFresh.setWarehouse(warehouse);

        warehouse.setSectionList(Collections.singletonList(sectionFresh));
        warehouse.setWarehouseManager(auth.getAdminUser());

        warehouse = warehouseRepository.save(warehouse);

        Product product1 = productRepository.save(ProductFactory.getFrozenProductA()); // Invalid Product
        Product product2 = productRepository.save(ProductFactory.getFreshProductA());

        List<BatchRequest> batchList = Arrays.asList(
                new BatchRequest(product1.getId(), 10F, 8, 8, null, LocalDate.now(), 10F),
                new BatchRequest(product2.getId(), 10F, 5, 5, null, LocalDate.now(), 12F)
        );
        return new InboundOrderRequest(null, sectionFresh.getId(), batchList);
    }

    public InboundOrderRequest getInstanceWithInvalidVolume(){
        // Returns an instance with a batch list with more volume than the section available volume
        // Section available volume: 20.0
        // Batch total volume: 30.0
        Warehouse warehouse = WarehouseFactory.getWarehouse();
        Section sectionFrozen = SectionFactory.getFrozenSection();
        sectionFrozen.setWarehouse(warehouse);

        warehouse.setSectionList(Collections.singletonList(sectionFrozen));
        warehouse.setWarehouseManager(auth.getAdminUser());

        warehouse = warehouseRepository.save(warehouse);

        Product product1 = productRepository.save(ProductFactory.getFrozenProductA());
        Product product2 = productRepository.save(ProductFactory.getFrozenProductB());

        // Batch total volume: 17.0 + 13.0 = 30.0
        List<BatchRequest> batchList = Arrays.asList(
                new BatchRequest(product1.getId(), 10F, 8, 8, null, LocalDate.now(), 17F),
                new BatchRequest(product2.getId(), 10F, 5, 5, null, LocalDate.now(), 13F)
        );
        return new InboundOrderRequest(null, sectionFrozen.getId(), batchList);
    }

    public InboundOrder getInboundOrderEntity(){
        // Returns a inbound order entity to be used in GET test
        Warehouse warehouse = WarehouseFactory.getWarehouse();
        Section sectionFresh = SectionFactory.getFreshSection();
        sectionFresh.setWarehouse(warehouse);

        warehouse.setSectionList(Collections.singletonList(sectionFresh));
        warehouse.setWarehouseManager(auth.getAdminUser());

        warehouse = warehouseRepository.save(warehouse);
        sectionRepository.save(sectionFresh);

        InboundOrder inboundOrder = new InboundOrder(null, null, null, sectionFresh);

        Product product1 = productRepository.save(ProductFactory.getFreshProductA());
        Product product2 = productRepository.save(ProductFactory.getFreshProductB());

        List<Batch> batchList = Arrays.asList(
                new Batch(null, 10F, 8, 8, null, LocalDate.now(), 10F, product1, null),
                new Batch(null, 10F, 5, 5, null, LocalDate.now(), 10F, product2, null)
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
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
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
        InboundOrder inboundOrder = repository.save(this.getInboundOrderEntity());

        // Perform the GET with the inboundOrder ID
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + inboundOrder.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        InboundOrderResponse responseDTO = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturned, InboundOrderResponse.class);

        assertEquals(inboundOrder.getId(), responseDTO.getId());
        assertEquals(inboundOrder.getSection().getId(), responseDTO.getSection().getId());
    }

    @Test
    public void testUpdateInboundOrder() throws Exception {
        // First creates an inbound order
        InboundOrder oldInboundOrder = repository.save(this.getInboundOrderEntity());
        InboundOrderRequest inboundOrderRequest = new InboundOrderRequest();
        Float oldSectionVolume = oldInboundOrder.getSection().getActualVolume();

        // Updating the batch list to only one product
        List<BatchRequest> newBatchList = Arrays.asList(
                new BatchRequest(oldInboundOrder.getBatchList().get(0).getProduct().getId(), 1F, 78, 8, null, LocalDate.now(), 9F)
        );
        inboundOrderRequest.setOrderDateTime(oldInboundOrder.getOrderDateTime());
        inboundOrderRequest.setSectionId(oldInboundOrder.getSection().getId());
        inboundOrderRequest.setBatchStock(newBatchList);

        String payloadRequest = writer.writeValueAsString(inboundOrderRequest);

        // Perform the PUT request
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + oldInboundOrder.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        InboundOrderResponse inboundOrderResponse = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturned, InboundOrderResponse.class);

        // The section volume should be updated
        assertNotEquals(oldSectionVolume, inboundOrderResponse.getSection().getActualVolume());
    }

    @Test
    public void testInvalidSectionTypeException() throws Exception {
        InboundOrderRequest inboundOrder = this.getInstanceWithInvalidProductType();

        String payloadRequest = writer.writeValueAsString(inboundOrder);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(jsonObjectReturned, ErrorDTO.class);

        assertEquals("InvalidSectionTypeException", errorDTO.getError());
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
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
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
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
                .content(payloadRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(jsonObjectReturned, ErrorDTO.class);

        assertEquals("InsufficientAvailableSpaceException", errorDTO.getError());
    }

    @Test
    public void testInboundOrderNotFound() throws Exception {
        // Perform the GET with a invalid ID
        String invalidId = "INVALID_ID";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + invalidId)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        ErrorDTO errorDTO = new ObjectMapper().readValue(jsonObjectReturned, ErrorDTO.class);

        assertEquals(errorDTO.getError(), "EntityNotFoundException");
        assertEquals(errorDTO.getDescription(), "Invalid inbound order ID: " + invalidId);
    }
}
