package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.SaleMapper;
import br.com.meli.fresh.dto.response.BatchProductResponse;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.services.ISaleService;
import br.com.meli.fresh.services.impl.SaleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/sale")
@AllArgsConstructor
@Validated
public class SaleController {

    private final SaleServiceImpl saleServiceImpl;
    private final ISaleService iSaleService;
    private final SaleMapper saleMapper;

    @GetMapping
    public ResponseEntity<?> getProductDueDate(@RequestParam(required = false) String section,
                                               @RequestParam(required = false) Integer numberWeeks){
        List<Batch> batchList = saleServiceImpl.getProductsDueDate();
        List<BatchProductResponse> result = batchList.stream().map(batch -> saleMapper.toResponseObject(batch)).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }


}
