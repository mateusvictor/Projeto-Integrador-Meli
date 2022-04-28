package br.com.meli.fresh.model.exception;

public class BuyerNotFoundException extends RuntimeException{
    public BuyerNotFoundException(String message) {
        super(message);
    }
}