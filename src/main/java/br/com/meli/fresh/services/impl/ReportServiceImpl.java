package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.dto.response.ReportResponse;
import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.exception.WarehouseNotFoundException;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IWarehouseRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl {

    private UserAuthenticatedService auth;

    private IWarehouseRepository warehouseRepository;

    private IBatchRepository batchRepository;

    private IUserRepository userRepository;

    public ReportResponse getBasicReport(String warehouseId) {
        // Checking authorization for the report
        UserSpringSecurity u = auth.authenticated();
        if(u == null && !u.hasRole(Role.ADMIN) || !u.hasRole(Role.WAREHOUSEMANAGER)) {
            throw new UserNotAllowedException("User not allowed the get the basic report.");
        }

        // Checking if the warehouse manager can be found on the database
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found!"));
        User warehouseManager = userRepository.findById(warehouse.getWarehouseManager().getId()).orElseThrow(() -> new UserNotFoundException(u.getId()));
        if(warehouseRepository.findById(warehouseId) == null) {
            throw new WarehouseNotFoundException("Id indicate do not found any warehouse.");
        }

        // Checking if the warehouse manager requester are the responsbile for the warehouse indicate in the id url.
        if(!warehouseRepository.findById(warehouseId).get().getWarehouseManager().getId().equals(warehouseManager.getId())) {
            throw new UserNotAllowedException("You are not responsible for this warehouse.");
        }

        // Find all the batches by the warehouse id
        List<Batch> batches = batchRepository.findAllByInboundOrder_Section_Warehouse_Id(warehouseRepository.findById(warehouseId).get().getId());

        Integer totalBatechesOnWarehouse = batches.size();
        Integer totalCountProducts = totalProducts(batches);
        Integer totalProductsFreshProducts = totalProductsByCategoryFilter(batches, "FS");
        Integer totalProductsRefrigeratedProducts = totalProductsByCategoryFilter(batches, "RF");
        Integer totalProductsFrozenProducts = totalProductsByCategoryFilter(batches, "FF");
        Integer totalProductsNextToExpired = calcProductsNextToExpired(batches);

        Double percentageOfFreshProducts = percentageCalc(Double.valueOf(totalCountProducts), Double.valueOf(totalProductsFreshProducts));
        Double percentageOfRefrigeratedProducts = percentageCalc(Double.valueOf(totalCountProducts), Double.valueOf(totalProductsRefrigeratedProducts));
        Double percentageOfFronzenProducts = percentageCalc(Double.valueOf(totalCountProducts), Double.valueOf(totalProductsFrozenProducts));

        return new ReportResponse(
                warehouseId,
                warehouseManager.getId(),
                warehouseRepository.findById(warehouseId).get().getSectionList().size(),
                totalBatechesOnWarehouse,
                totalCountProducts,
                totalProductsFreshProducts,
                percentageOfFreshProducts,
                totalProductsRefrigeratedProducts,
                percentageOfRefrigeratedProducts,
                totalProductsFrozenProducts,
                percentageOfFronzenProducts,
                totalProductsNextToExpired
                );
    }

    private Integer totalProductsByCategoryFilter(List<Batch> list, String categoryFilter) {
        return list.stream().reduce(0, (a, b) -> {
            if(b.getProduct().getCategory().equalsIgnoreCase(categoryFilter)) {
                return a + b.getCurrentQuantity();
            }
            return a;
        }, Integer::sum);
    }

    private Double percentageCalc(Double totalValue, Double quantity) {
        return 100 * quantity / totalValue;
    }

    private Integer calcProductsNextToExpired(List<Batch> list) {
        return list.stream().filter(b -> b.getDueDate().minusDays(7).compareTo(LocalDate.now()) <= 0 ).collect(Collectors.toList()).size();
    }

    private Integer totalProducts(List<Batch> list) {
        return list.stream().reduce(0, (a, b) -> a + b.getCurrentQuantity(), Integer::sum);
    }
}
