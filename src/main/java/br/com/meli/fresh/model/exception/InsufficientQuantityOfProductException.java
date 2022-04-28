package br.com.meli.fresh.model.exception;

public class InsufficientQuantityOfProductException extends RuntimeException{
    public InsufficientQuantityOfProductException(String message) {
        super(message);
    }
}
