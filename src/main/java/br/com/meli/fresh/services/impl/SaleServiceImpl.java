package br.com.meli.fresh.services.impl;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.exception.SaleProductNotFoundException;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.services.ISaleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.math.*;


@Service
@AllArgsConstructor
public class SaleServiceImpl implements ISaleService {
    private final IBatchRepository batchRepository;

    @Override
    public List<Batch> getAllProductsDueDate() {
        List<Batch> batches;

        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusWeeks(Long.valueOf(3));
        batches = batchRepository.findAllByDueDateBetweenOrderByDueDateAsc(now, endDate);

        //if (batches.size() == 0){
        //    throw new SaleProductNotFoundException("Sale product not found");
        //}
        LocalDate oneWeek = now.plusWeeks(1);
        LocalDate twoWeek = now.plusWeeks(2);

        batches.stream().map(batch -> {
            if(batch.getDueDate().isBefore(oneWeek)){
                BigDecimal price = batch.getProduct().getPrice();
                batch.getProduct().setPrice(price.multiply(BigDecimal.valueOf(0.8)).setScale(2,RoundingMode.HALF_EVEN));
            } else if (batch.getDueDate().isBefore(twoWeek)){
                BigDecimal price = batch.getProduct().getPrice();
                batch.getProduct().setPrice(price.multiply(BigDecimal.valueOf(0.9)).setScale(2,RoundingMode.HALF_EVEN));
            } else {
                BigDecimal price = batch.getProduct().getPrice();
                batch.getProduct().setPrice(price.multiply(BigDecimal.valueOf(0.95)).setScale(2,RoundingMode.HALF_EVEN));
            } return batch;

        }).collect(Collectors.toList());

        return batches;
    }
}