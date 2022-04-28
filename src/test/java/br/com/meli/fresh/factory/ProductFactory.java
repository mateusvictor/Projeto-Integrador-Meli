package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Product;

import java.util.List;
import java.util.UUID;

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

}
