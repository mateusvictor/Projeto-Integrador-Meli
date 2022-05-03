package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.services.ISectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements ISectionService {

    private final ISectionRepository sectionRepository;

    @Override
    public Section getByType(String type) {
        return sectionRepository.findByProductType(type);
    }
}
