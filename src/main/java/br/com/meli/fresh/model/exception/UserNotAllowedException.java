package br.com.meli.fresh.model.exception;

public class UserNotAllowedException extends RuntimeException{
    public UserNotAllowedException(String message) {
        super(message);
    }
}
