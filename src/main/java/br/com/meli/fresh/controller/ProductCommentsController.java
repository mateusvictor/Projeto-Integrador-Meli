package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.ProductCommentMapper;
import br.com.meli.fresh.dto.request.productRequest.ProductCommentRequestDTO;
import br.com.meli.fresh.dto.response.product.ProductCommentResponseDTO;
import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.ProductComment;
import br.com.meli.fresh.services.impl.ProductCommentServiceImpl;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/fresh-products/products/{id}/comment")
public class ProductCommentsController {

    private final ProductCommentMapper mapper;
    private final ProductCommentServiceImpl commentService;
    private final ProductServiceImpl productService;

    @PostMapping()
    public ResponseEntity<ProductCommentResponseDTO> createComment(@PathVariable String id, @RequestBody @Valid ProductCommentRequestDTO requestDTO, UriComponentsBuilder uriBuilder){
        ProductComment comment = this.commentService.create(this.mapper.toDomainObject(requestDTO), id);
        URI uri = uriBuilder
                .path("api/v1/fresh-products/products/{id}/comment/{idComment}")
                .buildAndExpand(id,comment.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.mapper.toResponseObject(comment));
    }

    @GetMapping()
    public ResponseEntity<Page<ProductCommentResponseDTO>> getAllCommentByProduct(@PathVariable String id, Pageable pageable){
        this.productService.getById(id);
        List<ProductCommentResponseDTO> comments =  this.commentService.getAll(id,pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
        Page<ProductCommentResponseDTO> pageComment = new PageImpl<>(comments);
        return ResponseEntity.ok(pageComment);
    }

    @GetMapping("/{idComment}")
    public ResponseEntity<ProductCommentResponseDTO> getCommentByIdComment(@PathVariable String id, @PathVariable String idComment){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.commentService.getById(id, idComment)));
    }

    @DeleteMapping("/{idComment}")
    public ResponseEntity<String> deleteCommentById(@PathVariable String id, @PathVariable String idComment){
        this.commentService.delete(id, idComment);
        return ResponseEntity.ok("Comment deleted!");
    }


}
