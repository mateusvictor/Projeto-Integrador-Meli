package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.BatchMapper;
import br.com.meli.fresh.assembler.InboundOrderMapper;
import br.com.meli.fresh.dto.request.BatchRequest;
import br.com.meli.fresh.dto.request.InboundOrderRequest;
import br.com.meli.fresh.dto.response.InboundOrderResponse;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/fresh-products/inboundorder")
@AllArgsConstructor
public class InboundOrderController {
    private final InboundOrderServiceImpl inboundOrderService;
    private final ISectionRepository sectionRepository;
    private final IProductRepository productRepository;

    private final InboundOrderMapper orderMapper;
    private final BatchMapper batchMapper;

    @ApiOperation(value = "This endpoint creates an inbound order thats supply all warehouse of the storage app.")
    @PostMapping()
    public ResponseEntity<InboundOrderResponse> createOrder(@Valid @RequestBody InboundOrderRequest inboundOrderRequest,
                                                            UriComponentsBuilder uriBuilder){
        InboundOrder inboundOrder = this.inboundOrderRequestToEntity(inboundOrderRequest);
        inboundOrder = inboundOrderService.create(inboundOrder);

        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(inboundOrder.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .body(orderMapper.toResponseObject(inboundOrder));
    }

    @ApiOperation(value = "This endpoint update an inbound order.")
    @PutMapping("/{id}")
    public ResponseEntity<InboundOrderResponse> updateOrder(@PathVariable String id,
                                                            @Valid @RequestBody InboundOrderRequest inboundOrderRequest,
                                                            UriComponentsBuilder uriBuilder){
        InboundOrder inboundOrder = this.inboundOrderRequestToEntity(inboundOrderRequest);
        inboundOrder = inboundOrderService.update(id, inboundOrder);

        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(inboundOrder.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .body(orderMapper.toResponseObject(inboundOrder));
    }

    @ApiOperation(value = "This endpoint can get an inbound ordered.")
    @GetMapping("/{id}")
    public ResponseEntity<InboundOrderResponse> getOrder(@PathVariable String id){
        return ResponseEntity.ok(
                orderMapper.toResponseObject(
                        inboundOrderService.getById(id)
                )
        );
    }

    private InboundOrder inboundOrderRequestToEntity(InboundOrderRequest inboundOrderRequest){
        // Method used to convert all DTOs nested in the InboundOrderRequest to entities
        // Used in POST and PUT endpoints

        // Converts non nested fields from inboundOrderRequest
        InboundOrder inboundOrder = orderMapper.toDomainObject(inboundOrderRequest);

        // Searches the section entity based on the ID provided in the DTO
        Section section = sectionRepository
                .findById(inboundOrderRequest.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid section ID: " + inboundOrderRequest.getSectionId()));

        // Converts the DTO batch list received to a entity batch list
        List<Batch> batchList = requestListToEntityList(inboundOrderRequest.getBatchStock());

        inboundOrder.setSection(section);
        inboundOrder.setBatchList(batchList);

        return inboundOrder;
    }

    private List<Batch> requestListToEntityList(List<BatchRequest> batchRequestList){
        // Converts List<BatchRequest> to List<Batch>
        //
        List<Batch> batchList = new ArrayList<>();
        for (BatchRequest batchRequest : batchRequestList){
            // Converts non nested models from batchRequest
            Batch newBatch = batchMapper.toDomainObject(batchRequest);

            // Searches the product entity based on the ID provided in the DTO
            Product batchProduct =  productRepository
                    .findById(batchRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid product ID: " + batchRequest.getProductId()));

            newBatch.setProduct(batchProduct);
            batchList.add(newBatch);
        }
        return batchList;
    }
}
