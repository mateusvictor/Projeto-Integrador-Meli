package br.com.meli.fresh.model.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(){
        super("This product already exists in our database");
    }
}
