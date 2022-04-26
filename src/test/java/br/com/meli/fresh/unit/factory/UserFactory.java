package br.com.meli.fresh.unit.factory;

import br.com.meli.fresh.model.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

public class UserFactory {


    public static Buyer createBuyer(){
        return new Buyer("1", "Joao", "joao@email.com", "password");
    }

    public static Page<Buyer> createPageBuyer(){
        List<Buyer>list = new ArrayList<>();
        Buyer buyerA = new Buyer("1", "Joao", "joao@email.com", "password");
        Buyer buyerB = new Buyer("2", "Maria", "maria@email.com", "password");
        Buyer buyerC = new Buyer("3", "Joaquina", "joaquina@email.com", "password");
        list.add(buyerA);
        list.add(buyerB);
        list.add(buyerC);
        return new PageImpl<>(list);
    }
}
