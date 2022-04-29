package br.com.meli.fresh.model.exception;

public class UserWithThisEmailAlreadyExists extends RuntimeException {
    public UserWithThisEmailAlreadyExists(String email) {
        super("This " + email + " is already owned by another user!");
    }
}
