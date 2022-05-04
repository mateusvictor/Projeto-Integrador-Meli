package br.com.meli.fresh.services;

import br.com.meli.fresh.model.Batch;
import java.util.List;

public interface ISaleService {
    List<Batch> getProductsDueDate();
}
