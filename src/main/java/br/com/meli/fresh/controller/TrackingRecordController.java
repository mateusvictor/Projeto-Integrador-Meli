package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.TrackingRecordMapper;
import br.com.meli.fresh.dto.request.TrackingRecordRequest;
import br.com.meli.fresh.dto.response.TrackingRecordResponse;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.model.TrackingRecord;
import br.com.meli.fresh.repository.IPurchaseOrderRepository;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import br.com.meli.fresh.services.impl.TrackingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/fresh-products/order-tracking")
public class TrackingRecordController {
    private final TrackingServiceImpl service;
    private final IPurchaseOrderRepository orderRepository;
    private final TrackingRecordMapper mapper;

    @GetMapping("/history/{orderId}")
    public ResponseEntity<List<TrackingRecordResponse>> getOrderTrackHistory(@PathVariable String orderId){
        return ResponseEntity.ok(
                mapper.toResponseObject(service.getTrackingRecordList(orderId))
        );
    }

    @PostMapping()
    public ResponseEntity<TrackingRecordResponse> createTrackingRecord(@RequestBody TrackingRecordRequest recordDTO,
                                                                       UriComponentsBuilder uriBuilder){
        TrackingRecord trackingRecord = service.create(this.requestDTOToEntity(recordDTO));

        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(trackingRecord.getId())
                .toUri();

        return ResponseEntity.created(uri).body(
                mapper.toResponseObject(trackingRecord)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackingRecordResponse> updateTrackingRecord(@PathVariable String id,
                                                                       @RequestBody TrackingRecordRequest recordDTO,
                                                                       UriComponentsBuilder uriBuilder){
        TrackingRecord trackingRecord = service.update(id, this.requestDTOToEntity(recordDTO));

        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(trackingRecord.getId())
                .toUri();

        return ResponseEntity.created(uri).body(
                mapper.toResponseObject(trackingRecord)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackingRecordResponse> getTrackingRecord(@PathVariable String id){
        TrackingRecord trackingRecord = service.getById(id);
        return ResponseEntity.ok(
                mapper.toResponseObject(trackingRecord)
        );
    }

    private TrackingRecord requestDTOToEntity(TrackingRecordRequest recordDTO){
        TrackingRecord trackingRecord = mapper.toDomainObject(recordDTO);
        PurchaseOrder order = orderRepository.findById(recordDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid orderId: " + recordDTO.getOrderId()));
        trackingRecord.setPurchaseOrder(order);
        return trackingRecord;
    }
}
