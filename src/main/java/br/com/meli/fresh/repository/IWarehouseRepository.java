package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWarehouseRepository extends JpaRepository<Warehouse, String> {
}
