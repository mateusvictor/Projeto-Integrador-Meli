package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBatchRepository extends JpaRepository<Batch, String> {

    List<Batch> findAllByProduct_Id(String id);
    Batch findByProduct_Id(String id);

}
