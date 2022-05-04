package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.UserMapper;
import br.com.meli.fresh.dto.request.UserRequestDTO;
import br.com.meli.fresh.dto.response.UserResponseDTO;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("api/v1/fresh-products/users")
@AllArgsConstructor
public class UserController {

    private final UserMapper mapper;
    private final UserServiceImpl service;

        @ApiOperation(value = "This endpoint can create a user for the api. The user can be 0, for Seller, 1 for Buyer, 2 for Warehouse Manager and 3 for Admin.")
        @PostMapping()
        public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO request, UriComponentsBuilder uriBuilder){
            User user = this.mapper.toDomainObject(request);
            user = this.service.create(user);
            URI uri = uriBuilder
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(this.mapper.toResponseObject(user));
        }

        @ApiOperation(value = "This endpoint can get all users.")
        @GetMapping()
        public ResponseEntity<Page<UserResponseDTO>> getAll(Pageable pageable){
            List<UserResponseDTO> list = this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
            Page<UserResponseDTO> pageList = new PageImpl<>(list);
            return ResponseEntity.ok(pageList);
        }

        @ApiOperation(value = "This endpoint can get an user by your id.")
        @GetMapping("/{id}")
        public ResponseEntity<UserResponseDTO> getById(@PathVariable String id){
            return ResponseEntity.ok(this.mapper.toResponseObject(this.service.getById(id)));
        }

        @ApiOperation(value = "This endpoint can update an user by sharing his id on the query url.")
        @PutMapping("/{id}")
        public ResponseEntity<UserResponseDTO> update(@PathVariable String id, @RequestBody UserRequestDTO requestDTO){
            return ResponseEntity.ok(this.mapper.toResponseObject(this.service.update(id, this.mapper.toDomainObject(requestDTO))));
        }

        @ApiOperation(value = "This endpoint can delete and user by sahring his id on the query url.")
        @DeleteMapping("/{id}")
        public ResponseEntity<String> delete(@PathVariable String id){
            this.service.delete(id);
            return ResponseEntity.ok("User deleted!");
        }
}
