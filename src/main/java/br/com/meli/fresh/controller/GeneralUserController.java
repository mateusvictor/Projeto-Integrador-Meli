package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.GeneralUserMapper;
import br.com.meli.fresh.dto.request.BuyerRequestDTO;
import br.com.meli.fresh.dto.request.GeneralUserRequestDTO;
import br.com.meli.fresh.dto.response.BuyerResponseDTO;
import br.com.meli.fresh.dto.response.GeneralUserResponseDTO;
import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.GeneralUser;
import br.com.meli.fresh.services.impl.GeneralUserServiceImpl;
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
@RequestMapping("api/v1/fresh-products/user")
@AllArgsConstructor
public class GeneralUserController{

    private final GeneralUserMapper mapper;
    private final GeneralUserServiceImpl service;

        @PostMapping()
        public ResponseEntity<GeneralUserResponseDTO> create(@RequestBody GeneralUserRequestDTO request, UriComponentsBuilder uriBuilder){
            GeneralUser user = this.mapper.toDomainObject(request);
            user = this.service.create(user);
            URI uri = uriBuilder
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(this.mapper.toResponseObject(user));
        }

        @GetMapping()
        public ResponseEntity<Page<GeneralUserResponseDTO>> getAll(Pageable pageable){
            List<GeneralUserResponseDTO> list = this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
            Page<GeneralUserResponseDTO> pageList = new PageImpl<>(list);
            return ResponseEntity.ok(pageList);
        }

        @GetMapping("/{id}")
        public ResponseEntity<GeneralUserResponseDTO> getById(@PathVariable String id){
            return ResponseEntity.ok(this.mapper.toResponseObject(this.service.getById(id)));
        }

        @PutMapping("/{id}")
        public ResponseEntity<GeneralUserResponseDTO> update(@PathVariable String id, @RequestBody GeneralUserRequestDTO requestDTO){
            return ResponseEntity.ok(this.mapper.toResponseObject(this.service.update(id, this.mapper.toDomainObject(requestDTO))));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> delete(@PathVariable String id){
            this.service.delete(id);
            return ResponseEntity.ok("Buyer deleted!");
        }
}
