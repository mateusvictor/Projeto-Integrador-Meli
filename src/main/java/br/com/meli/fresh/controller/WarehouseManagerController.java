package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.WarehouseManagerMapper;
import br.com.meli.fresh.dto.request.WarehouseManagerRequestDTO;
import br.com.meli.fresh.dto.response.WarehouseManagerResponseDTO;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.model.WarehouseManager;
import br.com.meli.fresh.services.impl.WarehouseManagerServiceImpl;
import br.com.meli.fresh.services.impl.WarehouseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/warehousemanger")
@AllArgsConstructor
public class WarehouseManagerController {

    private final WarehouseManagerServiceImpl service;
    private final WarehouseManagerMapper mapper;

    @PostMapping()
    public ResponseEntity<WarehouseManagerResponseDTO> create(@Valid @RequestBody WarehouseManagerRequestDTO request, UriComponentsBuilder uriBuilder){
        WarehouseManager entity = this.mapper.toDomainObject(request);
        entity = this.service.create(entity);
        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.mapper.toResponseObject(entity));
    }

    @GetMapping()
    public ResponseEntity<Page<WarehouseManagerResponseDTO>> getAll(Pageable pageable){
        List<WarehouseManagerResponseDTO> list = this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
        Page<WarehouseManagerResponseDTO> pageList = new PageImpl<>(list);
        return ResponseEntity.ok(pageList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseManagerResponseDTO> getById(@PathVariable String id){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseManagerResponseDTO> update(@PathVariable String id, @RequestBody WarehouseManagerRequestDTO requestDTO){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.update(id, this.mapper.toDomainObject(requestDTO))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        this.service.delete(id);
        return ResponseEntity.ok("Warehouse manager deleted!");
    }
}
