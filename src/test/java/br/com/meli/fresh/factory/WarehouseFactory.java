package br.com.meli.fresh.factory;

import br.com.meli.fresh.dto.request.WarehouseRequestDTO;
import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.unit.factory.UserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarehouseFactory {
    public static Warehouse getWarehouse(){

        return new Warehouse(null, "WAREHOUSE REPETIDO", null, null);
    }

    public static Warehouse createWarehouse(){
       Warehouse warehouse =  Warehouse.builder().name("teste")
                .sectionList(List.of(SectionFactory.createSectionDefault()))
                .warehouseManager(UserFactory.createWarehouseManagerDefault()).build();
       warehouse.getSectionList().get(0).setWarehouse(warehouse);
        return  warehouse;
    }

    public static Warehouse createWarehouseDefault(){
        return Warehouse.builder().id("1").name("teste")
                .sectionList(List.of(SectionFactory.createSection()))
                .warehouseManager(UserFactory.createWarehouseManagerDefault()).build();
    }

    public static Page<Warehouse> createPageWarehouse(){
        Warehouse warehouseA = createWarehouseDefault();
        Warehouse warehouseB = createWarehouseDefault();
        Warehouse warehouseC = createWarehouseDefault();
        List<Warehouse> list = new ArrayList<>();
        list.add(warehouseA);
        list.add(warehouseB);
        list.add(warehouseC);
        return new PageImpl<>(list);
    }

    public static WarehouseRequestDTO createWarehouseDTO(){
        WarehouseRequestDTO warehouseRequestDTO = WarehouseRequestDTO.builder().name(UUID.randomUUID().toString())
                .sectionList(List.of(SectionFactory.createSectionDefaultDTO())).build();
        return warehouseRequestDTO;
    }

    public static Warehouse getWarehouseB(){
        return new Warehouse(null, "FLORIPA - WAREHOUSE", null, null);
    }
}
