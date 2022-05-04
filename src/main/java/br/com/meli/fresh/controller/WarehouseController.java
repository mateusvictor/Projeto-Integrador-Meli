package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.WarehouseMapper;
import br.com.meli.fresh.dto.request.WarehouseRequestDTO;
import br.com.meli.fresh.dto.response.ProductQuantityResponse;
import br.com.meli.fresh.dto.response.WarehouseResponseDTO;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.services.impl.WarehouseServiceImpl;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("api/v1/fresh-products/warehouse")
@AllArgsConstructor
public class WarehouseController {

    private final WarehouseServiceImpl service;


    private final WarehouseMapper mapper;

    @ApiOperation(value = "This endpoint can get a warehouse by sharing your id.")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseByID(@PathVariable String id){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.getById(id)));
    }

    @ApiOperation(value = "This endpoint can get all warehouses.")
    @GetMapping()
    public ResponseEntity<Page<WarehouseResponseDTO>> getAllWarehousee(Pageable pageable){
        List<WarehouseResponseDTO> list = this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
        Page<WarehouseResponseDTO> pageList = new PageImpl<>(list);
        return ResponseEntity.ok(pageList);
    }

    @ApiOperation(value = "This endpoint can create a warehouse.")
    @PostMapping()
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(@Valid @RequestBody WarehouseRequestDTO requestDTO, UriComponentsBuilder uriBuilder){
        Warehouse entity = this.requestToEntity(requestDTO);
        Warehouse warehouseCreated = this.service.create(entity);
        URI uri = uriBuilder
                .path("api/v1/fresh-products/warehouse/{id}")
                .buildAndExpand(warehouseCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.mapper.toResponseObject(warehouseCreated));
    }

    private Warehouse requestToEntity(WarehouseRequestDTO requestDTO) {
        return this.mapper.toDomainObject(requestDTO);
    }

    @ApiOperation(value = "This endpoint can update a warehouse by sharing its id.")
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(@Valid @RequestBody WarehouseRequestDTO requestDTO, @PathVariable String id){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.update(id, this.mapper.toDomainObject(requestDTO))));
    }

    @ApiOperation(value = "This endpoint can delete a warehouse by sharing its id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable String id){
        this.service.delete(id);
        return ResponseEntity.ok("Warehouse deleted!");
    }

    @GetMapping("/productQuantity/{productId}")
    public ResponseEntity<ProductQuantityResponse> getProductQuantity(@PathVariable String productId){
        return ResponseEntity.ok(
                this.service.getProductQuantity(productId)
        );
    }

}
