package br.com.meli.fresh.services.exception;

public class InvalidSectionTypeException extends RuntimeException {
    public InvalidSectionTypeException(String message){
        super(message);
    }
}
