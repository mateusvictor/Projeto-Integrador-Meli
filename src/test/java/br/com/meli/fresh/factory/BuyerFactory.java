package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.User;

import java.util.Set;

public class BuyerFactory {

    public static User createBuyer() {
        User buyer = new User();
        buyer.setName("Gustavo");
        buyer.setEmail("email@gmail.com");
        buyer.setPassword("123");
        buyer.setRoles(Set.of(1));
        return buyer;
    }
}
