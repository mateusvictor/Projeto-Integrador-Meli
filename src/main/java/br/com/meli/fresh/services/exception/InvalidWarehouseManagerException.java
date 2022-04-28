package br.com.meli.fresh.services.exception;

public class InvalidWarehouseManagerException extends RuntimeException {
    public InvalidWarehouseManagerException(String message){
        super(message);
    }
}
