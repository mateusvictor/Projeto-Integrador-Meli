package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.Role;

import java.util.Set;

public class BuyerFactory {


    public static Buyer createBuyer() {
        Buyer buyer = new Buyer();
        buyer.setName("Gustavo");
        buyer.setEmail("email@gmail.com");
        buyer.setPassword("123");
        buyer.setRoles(Set.of(1));
        return buyer;
    }

}
