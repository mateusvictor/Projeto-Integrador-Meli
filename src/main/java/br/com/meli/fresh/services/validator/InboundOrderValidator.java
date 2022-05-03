package br.com.meli.fresh.services.validator;

import br.com.meli.fresh.model.*;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.exception.InsufficientAvailableSpaceException;
import br.com.meli.fresh.services.exception.InvalidSectionTypeException;
import br.com.meli.fresh.services.exception.InvalidWarehouseManagerException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InboundOrderValidator {
    private final InboundOrder inboundOrder;
    private final UserSpringSecurity user;

    public Boolean valid(){
        validateWarehouseManager();
        validateSectionType();
        validateSectionAvailableSpace();
        return true;
    }

    private void validateWarehouseManager(){
        // Checks the client user is the warehouse manager for the inbound order
        Warehouse orderWarehouse = inboundOrder.getSection().getWarehouse();

        // If the user is not logged in OR the user id doesn't match the warehouse manager for the warehouse specified, throw an error
        if (user == null || !user.getId().equalsIgnoreCase(orderWarehouse.getWarehouseManager().getId())){
            throw new InvalidWarehouseManagerException("You are not authorized to insert insert batches in the warehouse with ID: " + orderWarehouse.getId());
        }
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
