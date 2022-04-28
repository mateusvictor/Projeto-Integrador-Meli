package br.com.meli.fresh.model.exception;

public class WarehouseManagerNotFoundException extends RuntimeException{
    public WarehouseManagerNotFoundException(String message) {
        super(message);
    }
}
