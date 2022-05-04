package br.com.meli.fresh.services.impl;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.services.ISaleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleServiceImpl implements ISaleService {
    private final IBatchRepository batchRepository;

    @Override
    public List<Batch> getProductsDueDate() {
        List<Batch> batches = new ArrayList<>();

        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusWeeks(Long.valueOf(3));
        batches = batchRepository.findAllByDueDateBetweenOrderByDueDateAsc(now, endDate);

        LocalDate oneWeek = now.plusWeeks(1);
        LocalDate twoWeek = now.plusWeeks(2);

        batches.stream().map(batch -> {

            if(batch.getDueDate().isBefore(oneWeek)){
                BigDecimal price = batch.getProduct().getPrice();
                batch.getProduct().setPrice(price.multiply(new BigDecimal(0.8)));
            } else if (batch.getDueDate().isBefore(twoWeek)){
                BigDecimal price = batch.getProduct().getPrice();
                batch.getProduct().setPrice(price.multiply(new BigDecimal(0.9)));
            } else {
                BigDecimal price = batch.getProduct().getPrice();
                batch.getProduct().setPrice(price.multiply(new BigDecimal(0.95)));
            } return batch;

        }).collect(Collectors.toList());

        return batches;
    }
}
