package br.com.meli.fresh.model.exception;

public class InvalidEnumCartStatusException extends RuntimeException{
    public InvalidEnumCartStatusException(String message) {
        super(message);
    }
}
