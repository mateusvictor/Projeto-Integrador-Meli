package br.com.meli.fresh.unit.factory;

import br.com.meli.fresh.dto.request.BuyerRequestDTO;
import br.com.meli.fresh.model.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

public class UserFactory {


    public static Buyer createBuyerA(){
        return new Buyer("1", "Joao", "joazinho@email.com", "password");
    }
    public static Buyer createBuyerB(){
        return new Buyer("1", "Roberto", "robertinho@email.com", "password");
    }
    public static Buyer createBuyerC(){
        return new Buyer("1", "Luiza", "pedrinho@email.com", "password");
    }


    public static Buyer createBuyerDefault(){
        return new Buyer("1", "teste", "teste@email.com", "password");
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

    public static BuyerRequestDTO createBuyerRequestDto() {
        return BuyerRequestDTO.builder().name("Maria").email("teste123@email.com").password("password").build();
    }
    public static BuyerRequestDTO createBuyerRequestDtoToThrow() {
        return BuyerRequestDTO.builder().name("Maria").email("teste@email.com").password("password").build();
    }

    public static BuyerRequestDTO createBuyerToUpdateRequestDto() {
        return BuyerRequestDTO.builder().name("Paula").email("mudou@email.com").password("password").build();
    }

}
