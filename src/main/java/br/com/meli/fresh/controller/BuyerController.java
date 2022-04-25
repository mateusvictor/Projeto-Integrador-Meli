package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.BuyerMapper;
import br.com.meli.fresh.dto.request.BuyerRequestDTO;
import br.com.meli.fresh.dto.response.BuyerResponseDTO;
import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.services.impl.BuyerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/buyer")
@AllArgsConstructor
public class BuyerController {

    private final BuyerServiceImpl service;
    private final BuyerMapper mapper;

    @PostMapping()
    public ResponseEntity<BuyerResponseDTO> create(@RequestBody BuyerRequestDTO request, UriComponentsBuilder uriBuilder){
        Buyer buyer = this.mapper.toDomainObject(request);

        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(buyer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.mapper.toResponseObject(this.service.create(buyer)));
    }

    @GetMapping()
    public ResponseEntity<Page<BuyerResponseDTO>> getAll(Pageable pageable){
        Page<BuyerResponseDTO> page = new PageImpl<BuyerResponseDTO>(this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList()));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyerResponseDTO> getById()




}
