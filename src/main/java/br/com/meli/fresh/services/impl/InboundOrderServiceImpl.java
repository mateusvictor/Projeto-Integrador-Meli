package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IInboundOrderRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InboundOrderServiceImpl implements ICrudService<InboundOrder> {

    private final IInboundOrderRepository orderRepository;
    private final IBatchRepository batchRepository;

    @Override
    public InboundOrder create(InboundOrder inboundOrder) {
        /*
        TO-DO

        Validate Section
        Validate Batch List
        Add Exceptions
        ...
         */
        batchRepository.saveAll(inboundOrder.getBatchList());
        return orderRepository.save(inboundOrder);
    }

    @Override
    public InboundOrder update(String id, InboundOrder inboundOrder) {
        return null;
    }

    @Override
    public InboundOrder getById(String id) {
        return null;
    }

    @Override
    public Page<InboundOrder> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
