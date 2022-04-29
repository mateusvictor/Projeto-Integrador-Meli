package br.com.meli.fresh.model.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String id) {
        super("User not found in our database by the id:" + id);
    }
}
