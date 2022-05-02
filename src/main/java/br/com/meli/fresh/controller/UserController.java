package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.UserMapper;
import br.com.meli.fresh.dto.request.UserRequestDTO;
import br.com.meli.fresh.dto.response.UserResponseDTO;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.services.impl.UserServiceImpl;
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

        @GetMapping()
        public ResponseEntity<Page<UserResponseDTO>> getAll(Pageable pageable){
            List<UserResponseDTO> list = this.service.getAll(pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
            Page<UserResponseDTO> pageList = new PageImpl<>(list);
            return ResponseEntity.ok(pageList);
        }

        @GetMapping("/{id}")
        public ResponseEntity<UserResponseDTO> getById(@PathVariable String id){
            return ResponseEntity.ok(this.mapper.toResponseObject(this.service.getById(id)));
        }

        @PutMapping("/{id}")
        public ResponseEntity<UserResponseDTO> update(@PathVariable String id, @RequestBody UserRequestDTO requestDTO){
            return ResponseEntity.ok(this.mapper.toResponseObject(this.service.update(id, this.mapper.toDomainObject(requestDTO))));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> delete(@PathVariable String id){
            this.service.delete(id);
            return ResponseEntity.ok("User deleted!");
        }
}
