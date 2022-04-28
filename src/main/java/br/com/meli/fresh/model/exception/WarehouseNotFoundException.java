package br.com.meli.fresh.model.exception;

public class WarehouseNotFoundException extends RuntimeException{
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
