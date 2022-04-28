package br.com.meli.fresh.services.exception;

public class InsufficientAvailableSpaceException extends RuntimeException {
    public InsufficientAvailableSpaceException(String message){
        super(message);
    }
}
