package br.com.meli.fresh.services.impl;


import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IInboundOrderRepository;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.services.IBatchDueDateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class BatchDueDateServiceImpl implements IBatchDueDateService {

    private final IBatchRepository batchRepository;

    @Override
    public List<Batch> getProductsDueDate( Section section, LocalDate date) {

        List<Batch>  batches = new ArrayList<>();

//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.MONTH, date.getDayOfMonth());
//        c.set(Calendar.DAY_OF_MONTH, date.getMonthValue());
//        c.set(Calendar.YEAR, date.getYear());
//        int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        if(section != null) {
            batches = batchRepository.findAllByInboundOrder_SectionOrderByDueDateDesc(section);
        } else if (date != null) {
            batches = batchRepository.findAllByDueDateBefore(date);
        } else if (section != null && date != null) {
            batches = batchRepository.findAllByInboundOrder_SectionAndDueDateBeforeOrderByDueDate(section,  date);
        }

        return  batches;
    }
}
