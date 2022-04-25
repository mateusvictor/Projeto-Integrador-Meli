package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BatchServiceImpl implements ICrudService<Batch> {

    private final IBatchRepository repository;

    @Override
    public Batch create(Batch batch) {
        return null;
    }

    @Override
    public Batch update(String id, Batch batch) {
        return null;
    }

    @Override
    public Page<Batch> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
