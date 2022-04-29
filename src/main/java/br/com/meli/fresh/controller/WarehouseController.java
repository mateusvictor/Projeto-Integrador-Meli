package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.WarehouseMapper;
import br.com.meli.fresh.dto.request.WarehouseRequestDTO;
import br.com.meli.fresh.dto.response.WarehouseResponseDTO;
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

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/warehouse")
@AllArgsConstructor
public class WarehouseController {

    private final WarehouseServiceImpl service;

    private final WarehouseManagerServiceImpl warehouseManagerService;

    private final WarehouseMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseByID(@PathVariable String id){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.getById(id)));
    }

    @GetMapping()
    public ResponseEntity<Page<WarehouseResponseDTO>> getAllWarehousee(Pageable pageable){
        List<WarehouseResponseDTO> list = this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
        Page<WarehouseResponseDTO> pageList = new PageImpl<>(list);
        return ResponseEntity.ok(pageList);
    }


    @PostMapping()
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(@RequestBody WarehouseRequestDTO requestDTO, UriComponentsBuilder uriBuilder){
        Warehouse entity = this.requestToEntity(requestDTO);
        Warehouse warehouseCreated = this.service.create(entity);
        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.mapper.toResponseObject(warehouseCreated));
    }

    private Warehouse requestToEntity(WarehouseRequestDTO requestDTO) {
        if(requestDTO.getWarehouseManagerId()!=null){
            WarehouseManager manager = this.warehouseManagerService.getById(requestDTO.getWarehouseManagerId());
            Warehouse entity = this.mapper.toDomainObject(requestDTO);
            //entity.setWarehouseManager(manager);
            return entity;
        }
        return this.mapper.toDomainObject(requestDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(@RequestBody WarehouseRequestDTO requestDTO, @PathVariable String id){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.service.update(id, this.mapper.toDomainObject(requestDTO))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable String id){
        this.service.delete(id);
        return ResponseEntity.ok("Warehouse deleted!");
    }

}
