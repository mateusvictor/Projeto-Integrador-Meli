package br.com.meli.fresh.model.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String id) {
        super("Product was not found for the given ID! ID:" + id);
    }
}
