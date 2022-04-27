package br.com.meli.fresh.model.exception;

public class ProductsNotFoundException extends RuntimeException {
    public ProductsNotFoundException() {
        super("Products were not found!");
    }
}
