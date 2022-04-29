package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.WarehouseManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWarehouseManagerRepository extends JpaRepository<WarehouseManager, String> {
    WarehouseManager findByEmail(String email);
}
