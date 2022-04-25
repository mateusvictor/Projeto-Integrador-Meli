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
import br.com.meli.fresh.services.impl.InboundOrderServiceImpl;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import br.com.meli.fresh.services.impl.SectionServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/")
    public ResponseEntity<InboundOrderResponse> createOrder(@RequestBody InboundOrderRequest inboundOrderRequest){
        InboundOrder inboundOrder = orderMapper.toDomainObject(inboundOrderRequest);

        // Converts the section id received to a section entity
        Section section = sectionRepository.getById(inboundOrderRequest.getSectionId());

        // Converts the dto batch list received to a entity batch list
        List<Batch> batchList = requestListToEntityList(inboundOrderRequest.getBatchStock());

        inboundOrder.setSection(section);
        inboundOrder.setBatchList(batchList);

        return ResponseEntity.ok(
                orderMapper.toResponseObject(
                        inboundOrderService.create(inboundOrder))
        );
    }

    private List<Batch> requestListToEntityList(List<BatchRequest> batchRequestList){
        //
        // List<BatchRequest> --> List<Batch>
        //
        List<Batch> batchList = new ArrayList<>();
        for (BatchRequest batchRequest : batchRequestList){
            Batch newBatch = batchMapper.toDomainObject(batchRequest);
            newBatch.setId(null);
            newBatch.setProduct(
                    productRepository.getById(batchRequest.getProductId())
            );
            batchList.add(newBatch);

        }
        return batchList;
    }
}
