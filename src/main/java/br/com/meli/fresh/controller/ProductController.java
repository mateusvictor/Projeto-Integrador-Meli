package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.ProductMapper;
import br.com.meli.fresh.dto.request.productRequest.OnCreate;
import br.com.meli.fresh.dto.request.productRequest.ProductRequest;
import br.com.meli.fresh.dto.response.product.ProductResponse;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.filter.ProductFilter;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.stream.Collectors;
@RestController
@RequestMapping("api/v1/fresh-products/products")
@AllArgsConstructor
@Validated
public class ProductController {

    private final ProductServiceImpl service;
    private final ProductMapper mapper;

    @ApiOperation(value = "This endpoint can create a product.")
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

    @ApiOperation(value = "This endpoint can get a registered product.")
    @GetMapping("{id}")
    @ApiImplicitParam(name = "batch_order", value = "This query params can order the batches. Use L to order the batch by number id, C if you want to order by the current quantity and use F if you want to order by due date validation of the product on the batch.")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id, @RequestParam(required = false) String batch_order) {
        ProductResponse pRes = mapper.toResponseObject(service.getById(id));
        if(batch_order != null) orderingBatch(pRes, batch_order);
        return ResponseEntity.ok(pRes);
    }

    /**
     * This method realize the ordered batches inside the BatchList of the product.
     * You can order the batches of the product using the query param batch_order.
     * The options are: L to orderer by batch number id, C to orderer by current
     * quantity and F to orderer by due date validation.
     * Put the product response and the batch_order param in the params of the
     * method to realize the ordenation.
     * @param pRes receive the product response to be ordered by the batch_order query params.
     * @param batch_order the info order required by the client in the batch_order query params.
     *
     */
    private void orderingBatch(ProductResponse pRes, String batch_order) {
        if(batch_order.equalsIgnoreCase("L")) pRes.getBatchList().sort((a, b) -> a.getId().compareTo(b.getId()));
        if(batch_order.equalsIgnoreCase("C")) pRes.getBatchList().sort((b, a) -> a.getCurrentQuantity().compareTo(b.getCurrentQuantity()));
        if(batch_order.equalsIgnoreCase("F")) pRes.getBatchList().sort((a, b) -> a.getDueDate().compareTo(b.getDueDate()));
    }

    @ApiOperation(value = "This endpoint can get all products with several filters.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "With this query param, you can filter products by fresh (FS), refrigerated (RF) and frozen (FF).")
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(ProductFilter filter, Pageable pageable){
        return ResponseEntity.ok(
                new PageImpl<ProductResponse>(
                        service.getAll(filter, pageable).stream().map(mapper::toResponseObject).collect(Collectors.toList())
                )
        );
    }

    @ApiOperation(value = "This endpoint can update a product.")
    @PutMapping("{id}")
    public ResponseEntity<?> updateProductById(@PathVariable String id, @RequestBody ProductRequest productRequest){
        Product p = service.update(id, mapper.toDomainObject(productRequest));
        return ResponseEntity
                .ok()
                .header("Location", "http://localhost:8080/api/v1/fresh-products/products/" + p.getId())
                .build();
    }

    @ApiOperation(value = "This endpoint can delete a product. The service realize a logical operation, maintaining the data on the database.")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable String id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}


