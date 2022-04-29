package br.com.meli.fresh.model.exception;

public class WarehouseManagerCanNotBeDeletedException extends RuntimeException{
    public WarehouseManagerCanNotBeDeletedException(String message) {
        super(message);
    }
}
