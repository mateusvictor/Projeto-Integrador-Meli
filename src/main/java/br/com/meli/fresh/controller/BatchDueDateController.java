package br.com.meli.fresh.controller;


import br.com.meli.fresh.assembler.BatchDueDateMapper;
import br.com.meli.fresh.dto.response.BatchProductResponse;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.services.IBatchDueDateService;
import br.com.meli.fresh.services.ISectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/due-date")
@AllArgsConstructor
public class BatchDueDateController {

    private final IBatchDueDateService batchDueDateService;
    private final ISectionService sectionService;
    private final BatchDueDateMapper mapper;

    @GetMapping()
    public ResponseEntity<?> getProductsDueDate(@RequestParam String section) {
        Section sectionFound = sectionService.getByType(section);
        List<Batch> list = batchDueDateService.getProductsDueDate(sectionFound, LocalDate.now());

        List<BatchProductResponse> result = list.stream().map(batch -> mapper.toResponseObject(batch)).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }


}
