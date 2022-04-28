package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Warehouse;

public class WarehouseFactory {
    public static Warehouse getWarehouse(){
        return new Warehouse(null, "SP - WAREHOUSE", null, null);
    }
}
