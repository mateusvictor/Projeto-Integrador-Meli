package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Product;

public class ProductFactory {


    public static Product createProduct() {
        Product product = new Product();
        product.setName("Product");
        product.setCategory("FF");
        product.setMinTemperature(10f);
        product.setMinTemperature(10f);
        product.setWeight(10f);

        return product;
    }

    public static Product getFreshProductA(){
        return new Product(null, "Bolacha Trakinas", "fresco", 5F, 30F, 0.25F, null, null, true);
    }

    public static Product getFreshProductB(){
        return new Product(null, "Batata Doce", "fresco", 10F, 30F, 1F, null, null, true);
    }

    public static Product getFrozenProductA(){
        return new Product(null, "Pizza Quatro Queijos", "congelado", -20F, 30F, 0.25F, null, null, true);
    }

    public static Product getFrozenProductB(){
        return new Product(null, "Pao de Queijo", "congelado", -30F, 30F, 1F, null, null, true);
    }


}
