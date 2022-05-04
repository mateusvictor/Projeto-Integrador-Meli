package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Product;

import java.math.BigDecimal;

public class ProductFactory {


    public static Product createProduct() {
        Product product = new Product();
        product.setName("Product");
        product.setCategory("FF");
        product.setMinTemperature(10f);
        product.setMinTemperature(10f);
        product.setWeight(10f);
        product.setPrice(BigDecimal.valueOf(10));

        return product;
    }

    public static Product getFreshProductA(){
        return null;//return new Product("product1", "Bolacha Trakinas", "fresco", 5F, 30F, 0.25F, null, null, null, true);
    }

    public static Product getFreshProductB(){
        return null;//new Product("product2", "Batata Doce", "fresco", 10F, 30F, 1F, null, null, null, true);
    }

    public static Product getFrozenProductA(){
        return null;//new Product("product3", "Pizza Quatro Queijos", "congelado", -20F, 30F, 0.25F, null, null, null, true);
    }

    public static Product getFrozenProductB(){
        return null;//new Product(null, "Pao de Queijo", "congelado", -30F, 30F, 1F, null, null, null, true);
    }


}
