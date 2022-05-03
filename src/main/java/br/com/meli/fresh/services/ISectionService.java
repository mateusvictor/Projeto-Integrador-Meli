package br.com.meli.fresh.services;

import br.com.meli.fresh.model.Section;

public interface ISectionService {
    Section getByType(String type);
}
