package br.com.meli.fresh.services.validator;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.InboundOrder;
import br.com.meli.fresh.model.Section;
import br.com.meli.fresh.services.exception.InsufficientAvailableSpaceException;
import br.com.meli.fresh.services.exception.InvalidSectionTypeException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InboundOrderValidator {
    private final InboundOrder inboundOrder;

    public Boolean valid(){
        validateSectionType();
        validateSectionAvailableSpace();
        return true;
    }

    private void validateSectionType(){
        // Checks all products categories matches the section product type
        Section section = inboundOrder.getSection();
        for (Batch batch : inboundOrder.getBatchList()){
            // If the product type of one batch doesn't match the section product type, throw an exception
            if (!batch.getProduct().getCategory().equalsIgnoreCase(section.getProductType())){
                throw new InvalidSectionTypeException("Section type doesn't match the product type with id: " + batch.getProduct().getId());
            }
        }
    }

    private void validateSectionAvailableSpace(){
        // Checks the available space in the section is greater than or equals to the batches total volume
        Section section = inboundOrder.getSection();
        Double availableSpace = (double) (section.getMaxVolume() - section.getActualVolume());
        Double batchesTotalVolume = inboundOrder.calculateBatchesTotalVolume();

        // If the available space is less than the batches total volume, throw an exception
        if (availableSpace.compareTo(batchesTotalVolume) < 0){
            throw new InsufficientAvailableSpaceException("There is not enough space in the section provided." +
                    "\n Batches total volume: " + batchesTotalVolume +
                    "\n Section available space: " + availableSpace);
        }
    }

}
