package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISectionRepository extends JpaRepository<Section, String> {
    Section findByProductType(String type);
}
