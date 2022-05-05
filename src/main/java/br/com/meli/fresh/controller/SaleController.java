package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.SaleMapper;
import br.com.meli.fresh.dto.response.SaleResponse;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.services.impl.SaleServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/sale")
@AllArgsConstructor
@Validated
public class SaleController {

    private final SaleServiceImpl saleServiceImpl;
    private final SaleMapper saleMapper;

    @ApiOperation(value = "This endpoint can filter sale products.")
    @GetMapping()
    public ResponseEntity<?> getAllProductDueDate(){
        List<Batch> batchList = saleServiceImpl.getAllProductsDueDate();
        List<SaleResponse> result = batchList.stream().map(batch -> saleMapper.toResponseObject(batch)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
