package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.OrderStatus;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.TrackingRecord;
import br.com.meli.fresh.model.exception.InvalidEnumOrderStatusException;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.repository.IPurchaseOrderRepository;
import br.com.meli.fresh.repository.ITrackingRecordRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TrackingServiceImpl {
    private final ITrackingRecordRepository trackingRepository;
    private final IPurchaseOrderRepository purchaseOrderRepository;
    private final UserAuthenticatedService auth;

    public TrackingRecord createInitialTrackingRecord(PurchaseOrder order){
        // Method used to create the first tracking record for a order
        return trackingRepository.save(new TrackingRecord(
                null,
                LocalDateTime.now(),
                "TATUAPE / SP - WAREHOUSE",
                "Um representante esta preparando o seu pedido.",
                OrderStatus.PREPARING,
                order
        ));
    }

    public void validateUser(){
        UserSpringSecurity user = auth.authenticated(); // Get the user logged in

        // Checks if the user is a manager or a admin
        if (user == null || (!user.hasRole(Role.WAREHOUSEMANAGER) && !user.hasRole(Role.ADMIN)))
            throw new UserNotAllowedException("Only admins and managers can create/update tracking records");
    }

    public TrackingRecord create(TrackingRecord trackingRecord){
        // Creates a tracking record setting the date to now.
        validateUser();

        // Validates the enum field
        if (trackingRecord.getOrderStatus() == null)
            throw new InvalidEnumOrderStatusException("Invalid orderStatus parameter. Allowed: PREPARING, ON_THE_WAY, FINISHED, CANCELLED");

        trackingRecord.setDate(LocalDateTime.now());
        return trackingRepository.save(trackingRecord);
    }

    public TrackingRecord update(String id, TrackingRecord trackingRecordUpdated){
        // Updates only date, detailMessage and location of a TrackingRecord entity.
        validateUser();

        TrackingRecord trackingRecord = this.getById(id);
        trackingRecord.setDate(LocalDateTime.now());
        trackingRecord.setDetailMessage(trackingRecordUpdated.getDetailMessage());
        trackingRecord.setLocation(trackingRecord.getDetailMessage());

        return trackingRepository.save(trackingRecord);
    }

    public List<TrackingRecord> getTrackingRecordList(String orderId){
        // Returns all tracking records for a specific order sorted by date in descending order.
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid order ID: " + orderId));

        List<TrackingRecord> trackingRecordList = trackingRepository.findByPurchaseOrder(purchaseOrder);
        trackingRecordList.sort((b, a) -> a.getDate().compareTo(b.getDate()));
        return trackingRecordList;
    }

    public TrackingRecord getById(String id){
        return trackingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invalid tracking record ID: " + id));
    }
}
