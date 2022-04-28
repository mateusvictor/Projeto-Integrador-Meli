package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.Role;

public class BuyerFactory {


    public static Buyer createBuyer() {
        Buyer buyer = new Buyer();
        buyer.setName("Gustavo");
        buyer.setEmail("email@gmail.com");
        buyer.setPassword("123");
        buyer.setRole("ROLE_BUYER");
        return buyer;
    }

}
