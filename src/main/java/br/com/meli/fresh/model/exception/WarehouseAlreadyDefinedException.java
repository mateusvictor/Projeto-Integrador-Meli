package br.com.meli.fresh.model.exception;

public class WarehouseAlreadyDefinedException extends RuntimeException{
    public WarehouseAlreadyDefinedException(String message) {
        super(message);
    }
}
