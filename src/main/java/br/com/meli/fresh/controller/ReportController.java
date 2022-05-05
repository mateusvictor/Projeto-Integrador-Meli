package br.com.meli.fresh.controller;

import br.com.meli.fresh.dto.response.ReportResponse;
import br.com.meli.fresh.services.impl.ReportServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/fresh-products/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportServiceImpl service;

    @ApiOperation(value = "This endpoint get a basic report from the warehouse responsible by the warehouse manager.")
    @ApiParam(name = "id", value = "You need to inform the warehouse id that the warehouse is responsibl at the url.")
    @GetMapping("{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getBasicReport(id));
    }
}
