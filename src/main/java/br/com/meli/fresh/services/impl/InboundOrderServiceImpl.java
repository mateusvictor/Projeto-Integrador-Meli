package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IInboundOrderRepository;
import br.com.meli.fresh.repository.ISectionRepository;
import br.com.meli.fresh.services.ICrudService;
import br.com.meli.fresh.services.IInboundOrderService;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import br.com.meli.fresh.services.validator.InboundOrderValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InboundOrderServiceImpl implements IInboundOrderService<InboundOrder> {
    private final IInboundOrderRepository orderRepository;
    private final IBatchRepository batchRepository;
    private final ISectionRepository sectionRepository;

    @Override
    public InboundOrder create(InboundOrder inboundOrder) {
        // Checks the validations specified in the `validInboundOrder` method and then creates the batches,
        // updates the section volume and saves the inbound order
        InboundOrderValidator inboundOrderValidation = new InboundOrderValidator(inboundOrder);
        InboundOrder inboundOrderCreated = null;

        if (inboundOrderValidation.valid()){
            batchRepository.saveAll(inboundOrder.getBatchList());
            inboundOrderCreated = orderRepository.save(inboundOrder);

            // After creating the inbound order with a valid batch list, we have to add the inbound order to each batch,
            // to conclude the right insertion of a OneToMany relationship
            for (Batch batch : inboundOrder.getBatchList())
                batch.setInboundOrder(inboundOrderCreated);
            batchRepository.saveAll(inboundOrder.getBatchList());

            // Finally, the section volume is updated
            updateSectionActualVolume(inboundOrderCreated.getSection(), inboundOrder.calculateBatchesTotalVolume());
        }

        return inboundOrderCreated;
    }

    @Override
    public InboundOrder update(String id, InboundOrder inboundOrder) {
        InboundOrderValidator inboundOrderValidation = new InboundOrderValidator(inboundOrder);
        InboundOrder inboundOrderToUpdate = this.getById(id);
        Double oldTotalVolume = inboundOrderToUpdate.calculateBatchesTotalVolume();
        Section section = inboundOrderToUpdate.getSection();

        // Partially update the section actualVolume but only save it in updateSectionActualVolume
        section.setActualVolume(
                (float) (section.getActualVolume() - oldTotalVolume)
        );

        if (inboundOrderValidation.valid()){
            // Deletes the old batches associated with the inbound order
            batchRepository.deleteAll(inboundOrderToUpdate.getBatchList());

            // Saves the new list of batches
            batchRepository.saveAll(inboundOrder.getBatchList());

            // Updates the batch list in the order model
            inboundOrderToUpdate.setBatchList(inboundOrder.getBatchList());

            inboundOrderToUpdate = orderRepository.save(inboundOrderToUpdate);

            // Associate each new batch to the inboundOrderToUpdate
            for (Batch batch : inboundOrder.getBatchList())
                batch.setInboundOrder(inboundOrderToUpdate);
            batchRepository.saveAll(inboundOrder.getBatchList());

            // Finally, the section volume is updated
            updateSectionActualVolume(inboundOrderToUpdate.getSection(), inboundOrderToUpdate.calculateBatchesTotalVolume());
        }
        return inboundOrder;
    }

    private void updateSectionActualVolume(Section section, Double extraVolume){
        // Adds new batches total volume to the section actual volume
        section.setActualVolume(
                (float) (section.getActualVolume() + extraVolume)
        );
        sectionRepository.save(section);
    }

    @Override
    public InboundOrder getById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invalid inbound order ID: " + id));
    }
}
