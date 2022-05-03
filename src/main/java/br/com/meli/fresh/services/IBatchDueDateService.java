package br.com.meli.fresh.services;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;

import java.util.List;

public interface IBatchDueDateService {
    List<Batch> getProductsDueDate(Section section, Integer numberDays);
    List<Batch> getProductsDueDateByCategory(String category, Integer numberDays, String sort);
}
