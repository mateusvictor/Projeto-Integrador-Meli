package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Warehouse;
import br.com.meli.fresh.unit.factory.UserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

public class WarehouseFactory {
    public static Warehouse getWarehouse(){

        return new Warehouse(null, "SP - WAREHOUSE", null, null);
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

}
