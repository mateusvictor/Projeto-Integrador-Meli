package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.ProductCommentMapper;
import br.com.meli.fresh.dto.request.productRequest.ProductCommentRequestDTO;
import br.com.meli.fresh.dto.response.product.ProductCommentResponseDTO;
import br.com.meli.fresh.model.ProductComment;
import br.com.meli.fresh.services.impl.ProductCommentServiceImpl;
import br.com.meli.fresh.services.impl.ProductServiceImpl;
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

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/fresh-products/products/{idProduct}/comment")
public class ProductCommentsController {

    private final ProductCommentMapper mapper;
    private final ProductCommentServiceImpl commentService;
    private final ProductServiceImpl productService;


    @ApiOperation(value = "This endpoint creates a comment in a product.")
    @PostMapping()
    public ResponseEntity<ProductCommentResponseDTO> createComment(@PathVariable String idProduct, @RequestBody @Valid ProductCommentRequestDTO requestDTO, UriComponentsBuilder uriBuilder){
        ProductComment comment = this.commentService.create(this.mapper.toDomainObject(requestDTO), idProduct);
        URI uri = uriBuilder
                .path("api/v1/fresh-products/products/{id}/comment/{idComment}")
                .buildAndExpand(idProduct,comment.getId())
                .toUri();
        return ResponseEntity.created(uri).body(this.mapper.toResponseObject(comment));
    }
    @ApiOperation(value = "This endpoint get a comment from a product by Id.")
    @GetMapping()
    public ResponseEntity<Page<ProductCommentResponseDTO>> getAllCommentByProduct(@PathVariable String idProduct, Pageable pageable){
        this.productService.getById(idProduct);
        List<ProductCommentResponseDTO> comments =  this.commentService.getAll(idProduct,pageable).stream().map(this.mapper::toResponseObject).collect(Collectors.toList());
        Page<ProductCommentResponseDTO> pageComment = new PageImpl<>(comments);
        return ResponseEntity.ok(pageComment);
    }

    @ApiOperation(value = "This endpoint get all comments from a product.")
    @GetMapping("/{idComment}")
    public ResponseEntity<ProductCommentResponseDTO> getCommentByIdComment(@PathVariable String idProduct, @PathVariable String idComment){
        return ResponseEntity.ok(this.mapper.toResponseObject(this.commentService.getById(idProduct, idComment)));
    }

    @ApiOperation(value = "This endpoint delete a comment from a product.")
    @DeleteMapping("/{idComment}")
    public ResponseEntity<String> deleteCommentById(@PathVariable String idProduct, @PathVariable String idComment){
        this.commentService.delete(idProduct, idComment);
        return ResponseEntity.ok("Comment deleted!");
    }


}
