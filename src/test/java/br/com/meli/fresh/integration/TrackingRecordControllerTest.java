package br.com.meli.fresh.integration;

import br.com.meli.fresh.assembler.TrackingRecordMapper;
import br.com.meli.fresh.dto.request.TrackingRecordRequest;
import br.com.meli.fresh.dto.response.InboundOrderResponse;
import br.com.meli.fresh.dto.response.TrackingRecordResponse;
import br.com.meli.fresh.factory.AuthFactory;
import br.com.meli.fresh.model.OrderStatus;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.model.TrackingRecord;
import br.com.meli.fresh.repository.IPurchaseOrderRepository;
import br.com.meli.fresh.repository.ITrackingRecordRepository;
import br.com.meli.fresh.services.impl.TrackingServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
@Rollback
public class TrackingRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrackingRecordMapper mapper;

    @Autowired
    private TrackingServiceImpl service;

    @Autowired
    private IPurchaseOrderRepository orderRepository;

    @Autowired
    private ITrackingRecordRepository trackingRepository;

    @Autowired
    private AuthFactory auth;

    // Used to make POST and
    private final ObjectWriter writer = new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .registerModule(new JavaTimeModule())
            .writer().withDefaultPrettyPrinter();


    private final String BASE_URL = "/api/v1/fresh-products/order-tracking";

    @Test
    public void testGetTrackingRecord() throws Exception {
        PurchaseOrder order = orderRepository.save(new PurchaseOrder(null, LocalDateTime.now(), null, null));
        TrackingRecord trackingRecord = new TrackingRecord(null, LocalDateTime.now(), "SAO PAULO", "Seu pedido saiu para entrega", OrderStatus.ON_THE_WAY, order);
        trackingRecord = trackingRepository.save(trackingRecord);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + trackingRecord.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        TrackingRecordResponse responseDTO = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturned, TrackingRecordResponse.class);

        assertEquals(trackingRecord.getId(), responseDTO.getId());
    }

    @Test
    public void testCreateTrackingRecord() throws Exception {
        PurchaseOrder order = orderRepository.save(new PurchaseOrder(null, LocalDateTime.now(), null, null));
        TrackingRecordRequest requestDTO = new TrackingRecordRequest("MOOCA - SAO PAULO / SP", "Seu pedido esta em rota de entrega", OrderStatus.ON_THE_WAY.toString(), order.getId());
        String jsonPayload = writer.writeValueAsString(requestDTO);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
                .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String locationUrl = mvcResult.getResponse().getHeader("Location");
        assertNotNull(locationUrl);
    }

    @Test
    public void testUpdateTrackingRecord() throws Exception {
        PurchaseOrder order = orderRepository.save(new PurchaseOrder(null, LocalDateTime.now(), null, null));
        TrackingRecord trackingRecord = trackingRepository.save(new TrackingRecord(null, LocalDateTime.now(), "SAO PAULO", "Seu pedido saiu para entrega", OrderStatus.ON_THE_WAY, order));
        String oldDetailMessage = trackingRecord.getLocation();
        TrackingRecordRequest requestDTO = new TrackingRecordRequest();

        requestDTO.setLocation(trackingRecord.getLocation());
        requestDTO.setDetailMessage("O seu pedido mudou de transporte.");
        requestDTO.setOrderStatus(trackingRecord.getOrderStatus().toString());
        requestDTO.setOrderId(order.getId());

        String jsonPayload = writer.writeValueAsString(requestDTO);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + trackingRecord.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc))
                .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        TrackingRecordResponse recordResponse = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturned, TrackingRecordResponse.class);

        // The detail message should be updated
        assertNotEquals(oldDetailMessage, recordResponse.getDetailMessage());
    }

    @Test
    public void testGetTrackingRecordList() throws Exception {
        PurchaseOrder order = orderRepository.save(new PurchaseOrder(null, LocalDateTime.now(), null, null));
        TrackingRecord record1 = trackingRepository.save(new TrackingRecord(null, LocalDateTime.now(), "SAO PAULO", "Seu pedido saiu para entrega.", OrderStatus.ON_THE_WAY, order));
        TrackingRecord record2 = trackingRepository.save(new TrackingRecord(null, LocalDateTime.now().plusSeconds(10L), "SAO PAULO - ENG. GOULART", "Seu pedido chegou no seu bairro.", OrderStatus.ON_THE_WAY, order));

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/history/" + order.getId())
                .header(HttpHeaders.AUTHORIZATION, auth.token(mockMvc)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonObjectReturned = mvcResult.getResponse().getContentAsString();
        List<TrackingRecordResponse> responseDTO = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(jsonObjectReturned, new TypeReference<List<TrackingRecordResponse>>(){});

        assertEquals(2, responseDTO.size());
        assertEquals(record2.getId(), responseDTO.stream().findFirst().orElse(null).getId());
    }
}
