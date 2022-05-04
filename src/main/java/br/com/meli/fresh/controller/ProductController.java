package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.ProductMapper;
import br.com.meli.fresh.dto.request.productRequest.OnCreate;
import br.com.meli.fresh.dto.request.productRequest.ProductRequest;
import br.com.meli.fresh.dto.response.product.ProductBatchResponse;
import br.com.meli.fresh.dto.response.product.ProductResponse;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.filter.ProductFilter;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.stream.Collectors;
@RestController
@RequestMapping("api/v1/fresh-products/products")
@AllArgsConstructor
@Validated
public class ProductController {

    private final ProductServiceImpl service;
    private final ProductMapper mapper;

    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequest productRequest){
        Product p = service.create(mapper.toDomainObject(productRequest));
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(p.getId())
                .toUri())
                .build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id, @RequestParam(required = false) String batch_order) {
        ProductResponse pRes = mapper.toResponseObject(service.getById(id));
        if(batch_order != null) orderingBatch(pRes, batch_order);
        return ResponseEntity.ok(pRes);
    }

    private void orderingBatch(ProductResponse pRes, String batch_order) {
        if(batch_order.equalsIgnoreCase("L")) pRes.getBatchList().sort(Comparator.comparing(ProductBatchResponse::getId));
        if(batch_order.equalsIgnoreCase("C")) pRes.getBatchList().sort(Comparator.comparing(ProductBatchResponse::getCurrentQuantity));
        if(batch_order.equalsIgnoreCase("F")) pRes.getBatchList().sort(Comparator.comparing(ProductBatchResponse::getDueDate));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(ProductFilter filter, Pageable pageable){
        return ResponseEntity.ok(
                new PageImpl<ProductResponse>(
                        service.getAll(filter, pageable).stream().map(mapper::toResponseObject).collect(Collectors.toList())
                )
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProductById(@PathVariable String id, @RequestBody ProductRequest productRequest){
        Product p = service.update(id, mapper.toDomainObject(productRequest));
        return ResponseEntity
                .ok()
                .header("Location", "http://localhost:8080/api/v1/fresh-products/products/" + p.getId())
                .build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable String id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}


