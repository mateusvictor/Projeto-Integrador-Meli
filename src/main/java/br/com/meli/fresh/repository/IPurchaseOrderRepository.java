package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
}
