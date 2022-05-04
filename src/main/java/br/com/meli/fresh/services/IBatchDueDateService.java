package br.com.meli.fresh.services;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface IBatchDueDateService {
    List<Batch> getProductsDueDate(Section section, LocalDate date);
}
