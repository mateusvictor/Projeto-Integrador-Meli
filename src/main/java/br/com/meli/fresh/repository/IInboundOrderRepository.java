package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInboundOrderRepository extends JpaRepository<InboundOrder, String> {
}
