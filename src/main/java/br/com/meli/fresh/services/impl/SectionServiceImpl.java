package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SectionServiceImpl implements ICrudService<Section> {

    private final ISectionRepository repository;

    @Override
    public Section create(Section section) {
        return null;
    }

    @Override
    public Section update(String id, Section section) {
        return null;
    }

    @Override
    public Section getById(String id) {
        return null;
    }

    @Override
    public Page<Section> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
