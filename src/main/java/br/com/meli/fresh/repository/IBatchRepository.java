package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBatchRepository extends JpaRepository<Batch, String> {
}
