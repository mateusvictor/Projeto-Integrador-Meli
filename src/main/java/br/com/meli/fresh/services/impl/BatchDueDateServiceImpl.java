package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.services.IBatchDueDateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

@Service
@AllArgsConstructor
public class BatchDueDateServiceImpl implements IBatchDueDateService {

    private final IBatchRepository batchRepository;

    @Override
    public List<Batch> getProductsDueDate(Section section, Integer numberDays) {
        List<Batch> batches = new ArrayList<>();

        if (numberDays != null) {
            LocalDate now = LocalDate.now();
            LocalDate endDate = now.plusDays(Long.valueOf(numberDays));

            if (section != null) {
                batches = batchRepository.findAllByInboundOrder_SectionAndDueDateBetweenOrderByDueDateAsc(section, now, endDate);
            } else {
                batches = batchRepository.findAllByDueDateBetweenOrderByDueDateAsc(now, endDate);
            }

        } else if (section != null) {
            batches = batchRepository.findAllByInboundOrder_SectionOrderByDueDateAsc(section);
        }

        return batches;
    }

    @Override
    public List<Batch> getProductsDueDateByCategory(String category, Integer numberDays, String sort) {
        List<Batch> batches = new ArrayList<>();

        if (numberDays != null) {
            LocalDate now = LocalDate.now();
            LocalDate endDate = now.plusDays(Long.valueOf(numberDays));

            if(category != null) {
                batches = batchRepository.findAllByProduct_CategoryAndDueDateBetweenOrderByDueDateAsc(category, now, endDate);
            } else {
                batches = batchRepository.findAllByDueDateBetweenOrderByDueDateAsc(now, endDate);
            }

        } else if(category != null) {
            batches = batchRepository.findAllByProduct_CategoryOrderByDueDateAsc(category);
        }

       if(sort != null && sort == "desc") {
           batches = batches.stream().sorted(Comparator.comparing(Batch::getDueDate, reverseOrder())).collect(Collectors.toList());
       }

        return batches;
    }
}
