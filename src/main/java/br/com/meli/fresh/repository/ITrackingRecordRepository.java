package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.model.TrackingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITrackingRecordRepository extends JpaRepository<TrackingRecord, String> {
    List<TrackingRecord> findByPurchaseOrder(PurchaseOrder purchaseOrder);
}
