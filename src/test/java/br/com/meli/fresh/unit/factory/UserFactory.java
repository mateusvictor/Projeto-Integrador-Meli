package br.com.meli.fresh.unit.factory;

import br.com.meli.fresh.dto.request.BuyerRequestDTO;
import br.com.meli.fresh.dto.request.SellerRequestDTO;
import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserFactory {


    public static Buyer createBuyerA() {
        return new Buyer("1", "Joao", "joazinho@email.com", "password", Set.of(1));
    }

    public static Buyer createBuyerB() {
        return new Buyer("1", "Roberto", "robertinho@email.com", "password", Set.of(1));
    }

    public static Buyer createBuyerC() {
        return new Buyer("1", "Luiza", "pedrinho@email.com", "password", Set.of(1));
    }

    public static Buyer createBuyerD() {
        return new Buyer("1", "Caio", "caio@email.com", "password", Set.of(1));
    }

    public static Buyer createBuyerE() {
        return new Buyer("1", "Vinicius", "vinicius@email.com", "password", Set.of(1));
    }


    public static Buyer createBuyerDefault() {
        return new Buyer("1", "teste", "teste@email.com", "password", Set.of(1));
    }

    public static Page<Buyer> createPageBuyer() {
        List<Buyer> list = new ArrayList<>();
        Buyer buyerA = new Buyer("1", "Joao", "joao@email.com", "password", Set.of(1));
        Buyer buyerB = new Buyer("2", "Maria", "maria@email.com", "password", Set.of(1));
        Buyer buyerC = new Buyer("3", "Joaquina", "joaquina@email.com", "password", Set.of(1));
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


    /// Cenario de testes Sellers
    public static Seller createSellerA() {
        return new Seller("1", "Tony", "tony@email.com", "password", Set.of(0));
    }

    public static Seller createSellerB() {
        return new Seller("1", "Bruce", "bruce@email.com", "password", Set.of(0));
    }

    public static Seller createSellerC() {
        return new Seller("1", "Peter", "peter@email.com", "password", Set.of(0));
    }

    public static Seller createSellerD() {
        return new Seller("1", "Caio", "caio@email.com", "password", Set.of(0));
    }

    public static Seller createSellerE() {
        return new Seller("1", "Vinicius", "vinicius@email.com", "password", Set.of(0));
    }

    public static Seller createSeller() {
        return new Seller("1", "Cirilo", "cirilo@email.com", "password", Set.of(0));
    }

    public static Page<Seller> createPageSeller() {
        List<Seller> list = new ArrayList<>();
        Seller sellerA = new Seller("1", "Cirilo", "Cirilo@email.com", "password", Set.of(0));
        Seller sellerB = new Seller("2", "Maria Joaquina", "mariajoaquina@email.com", "password", Set.of(0));
        Seller sellerC = new Seller("3", "Jaime", "Jaime@email.com", "password", Set.of(0));
        list.add(sellerA);
        list.add(sellerB);
        list.add(sellerC);
        return new PageImpl<>(list);
    }

    public static SellerRequestDTO createSellerRequestDto() {
        return SellerRequestDTO.builder().name("Mario").email("mario@email.com").password("password").build();
    }

    public static SellerRequestDTO createSellerRequestDtoToThrow() {
        return SellerRequestDTO.builder().name("Luigi").email("luigi@email.com").password("password").build();
    }

    public static SellerRequestDTO createSellerToUpdateRequestDto() {
        return SellerRequestDTO.builder().name("Peach").email("peach@email.com").password("password").build();
    }
}



