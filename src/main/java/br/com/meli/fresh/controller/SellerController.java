package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.SellerMapper;
import br.com.meli.fresh.dto.request.SellerRequestDTO;
import br.com.meli.fresh.dto.response.SellerResponseDTO;
import br.com.meli.fresh.model.Seller;
import br.com.meli.fresh.services.impl.SellerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/seller")
@AllArgsConstructor
public class SellerController {

    private final SellerServiceImpl service;
    private final SellerMapper mapper;

    @PostMapping()
    public ResponseEntity<SellerResponseDTO> create(@RequestBody SellerRequestDTO request, UriComponentsBuilder uriBuilder) {
        Seller seller = this.mapper.toDomainObject(request);
        seller = this.service.create(seller);
        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(seller.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.mapper.toResponseObject(seller));
    }

    @GetMapping()
    public ResponseEntity<Page<SellerResponseDTO>> getAll(Pageable pageable) {
        List<SellerResponseDTO> list = this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
        Page<SellerResponseDTO> pageList = new PageImpl<>(list);
        return ResponseEntity.ok(pageList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> update(@PathVariable String id, @RequestBody SellerRequestDTO requestDTO) {
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.update(id, this.mapper.toDomainObject(requestDTO))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        this.service.delete(id);
        return ResponseEntity.ok("Buyer deleted!");
    }
}