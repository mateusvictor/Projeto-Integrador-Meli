package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.OrderStatus;
import br.com.meli.fresh.model.ProductOrder;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements ICrudService<PurchaseOrder> {


    @Override
    public PurchaseOrder create(PurchaseOrder purchaseOrder) {
        return null;
    }

    @Override
    public PurchaseOrder update(String id, PurchaseOrder purchaseOrder) {
        return null;
    }

    @Override
    public PurchaseOrder getById(String id) {
        return null;
    }

    @Override
    public Page<PurchaseOrder> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    public void createOrder(OrderStatus status, ProductOrder order, String buyerId){

    }
}
