package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.ProductMapper;
import br.com.meli.fresh.dto.request.ProductRequest;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/fresh-products/product")
@AllArgsConstructor
public class ProductController {

    private final ProductServiceImpl service;
    private final ProductMapper mapper;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequest productRequest){
        Product p = service.create(mapper.toDomainObject(productRequest));
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(p.getId())
                .toUri())
                .build();

    }
}
