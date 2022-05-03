package br.com.meli.fresh.controller;


import br.com.meli.fresh.assembler.BatchDueDateMapper;
import br.com.meli.fresh.dto.response.BatchProductResponse;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.services.IBatchDueDateService;
import br.com.meli.fresh.services.ISectionService;
import br.com.meli.fresh.services.impl.BatchDueDateServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/due-date")
@AllArgsConstructor
public class BatchDueDateController {

    private final BatchDueDateServiceImpl batchDueDateService;
    private final ISectionService sectionService;
    private final BatchDueDateMapper mapper;

    @GetMapping()
    public ResponseEntity<?> getProductsDueDate(@RequestParam(required = false) String section, @RequestParam(required = false) Integer numberDays) {
        Section sectionFound = sectionService.getByType(section);
        List<Batch> list = batchDueDateService.getProductsDueDate(sectionFound, numberDays);

        List<BatchProductResponse> result = list.stream().map(batch -> mapper.toResponseObject(batch)).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getProductsDueDateByCategory(
                        @RequestParam(required = false) String category,
                        @RequestParam(required = false) Integer numberDays,
                        @RequestParam(required = false) String sort
    ) {
        List<Batch> list = batchDueDateService.getProductsDueDateByCategory(category, numberDays, sort);

        List<BatchProductResponse> result = list.stream().map(batch -> mapper.toResponseObject(batch)).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }



}
