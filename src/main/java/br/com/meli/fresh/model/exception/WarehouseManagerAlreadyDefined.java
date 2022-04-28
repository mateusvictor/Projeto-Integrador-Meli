package br.com.meli.fresh.model.exception;

public class WarehouseManagerAlreadyDefined extends RuntimeException{
    public WarehouseManagerAlreadyDefined(String message) {
        super(message);
    }
}
