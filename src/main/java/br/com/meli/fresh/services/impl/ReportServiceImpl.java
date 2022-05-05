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
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
        User warehouseManager = userRepository.findById(u.getId()).orElseThrow(() -> new UserNotFoundException(u.getId()));
        if(warehouseRepository.findByWarehouseManager(warehouseManager) == null) {
            throw new WarehouseNotFoundException("Id indicate do not found any warehouse.");
        }

        // Checking if the warehouse manager requester are the responsbile for the warehouse indicate in the id url.
        if(!warehouseRepository.findByWarehouseManager(warehouseManager).getWarehouseManager().getId().equals(warehouseManager.getId())) {
            throw new UserNotAllowedException("You are not responsible for this warehouse.");
        }

        // Find all the batches by the warehouse id
        List<Batch> batches = batchRepository.findAllByInboundOrder_Section_Warehouse_Id(warehouseRepository.findByWarehouseManager(warehouseManager).getId());

        Integer totalBatechesOnWarehouse = batches.size();
        Integer totalProductsFreshProducts = totalProductsByCategoryFilter(batches, "FS");
        Integer totalProductsRefrigeratedProducts = totalProductsByCategoryFilter(batches, "RF");
        Integer totalProductsFrozenProducts = totalProductsByCategoryFilter(batches, "FF");
        Integer totalProductsNextToExpired = calcProductsNextToExpired(batches);

        Double percentageOfFreshProducts = percentageCalc(Double.valueOf(totalBatechesOnWarehouse), Double.valueOf(totalProductsFreshProducts));
        Double percentageOfRefrigeratedProducts = percentageCalc(Double.valueOf(totalBatechesOnWarehouse), Double.valueOf(totalProductsRefrigeratedProducts));
        Double percentageOfFronzenProducts = percentageCalc(Double.valueOf(totalBatechesOnWarehouse), Double.valueOf(totalProductsFrozenProducts));

        return new ReportResponse(
                warehouseId,
                warehouseManager.getId(),
                warehouseRepository.findByWarehouseManager(warehouseManager).getSectionList().size(),
                batches.size(),
                totalProducts(batches),
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
        return list.stream().filter(b -> b.getProduct().getCategory().equalsIgnoreCase(categoryFilter)).collect(Collectors.toList()).size();

    }

    private Double percentageCalc(Double totalValue, Double quantity) {
        return 100 * quantity / totalValue;
    }

    private Integer calcProductsNextToExpired(List<Batch> list) {
        return list.stream().filter(b -> b.getDueDate().plusDays(-7).compareTo(LocalDate.now().plusDays(-7)) == 0).collect(Collectors.toList()).size();
    }

    private Integer totalProducts(List<Batch> list) {
        return list.stream().reduce(0, (a, b) -> a + b.getCurrentQuantity(), Integer::sum);
    }
}
