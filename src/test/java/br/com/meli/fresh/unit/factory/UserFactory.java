package br.com.meli.fresh.unit.factory;

import br.com.meli.fresh.dto.request.UserRequestDTO;
import br.com.meli.fresh.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserFactory {


    public static User createUserBuyerA() {
        return new User("1", "Joao", "joazinho@email.com", "password", Set.of(1));
    }

    public static User createUserBuyerB() {
        return new User("1", "Roberto", "robertinho@email.com", "password", Set.of(1));
    }

    public static User createUserBuyerC() {
        return new User("1", "Luiza", "pedrinho@email.com", "password", Set.of(1));
    }

    public static User createUserBuyerD() {
        return new User("1", "Caio", "caio@email.com", "password", Set.of(1));
    }

    public static User createUserBuyerE() {
        return new User("1", "Vinicius", "vinicius@email.com", "password", Set.of(1));
    }


    public static User createUserBuyerDefault() {
        return new User("1", "teste", "teste@email.com", "password", Set.of(1));
    }

    public static Page<User> createPageUserBuyers() {
        List<User> list = new ArrayList<>();
        User UserA = new User("1", "Joao", "joao@email.com", "password", Set.of(1));
        User UserB = new User("2", "Maria", "maria@email.com", "password", Set.of(1));
        User UserC = new User("3", "Joaquina", "joaquina@email.com", "password", Set.of(1));
        list.add(UserA);
        list.add(UserB);
        list.add(UserC);
        return new PageImpl<>(list);
    }

    public static UserRequestDTO createBuyerUserRequestDto() {
        return UserRequestDTO.builder().name("Maria").email("teste123@email.com").password("password").roles(Set.of(1)).build();
    }

    public static UserRequestDTO createBuyerUserRequestDtoToThrow() {
        return UserRequestDTO.builder().name("Maria").email("teste@email.com").password("password").roles(Set.of(1)).build();
    }

    public static UserRequestDTO createBuyerUserToUpdateRequestDto() {
        return UserRequestDTO.builder().name("Paula").email("mudou@email.com").password("password").roles(Set.of(1)).build();
    }


    /// Cenario de testes Seller
    public static User createUserSellerA() {
        return new User("1", "Tony", "tony@email.com", "password", Set.of(0));
    }

    public static User createUseSellerB() {
        return new User("1", "Bruce", "bruce@email.com", "password", Set.of(0));
    }

    public static User createUserSellerC() {
        return new User("1", "Peter", "peter@email.com", "password", Set.of(0));
    }

    public static User createUserSellerD() {
        return new User("1", "Caio", "caio@email.com", "password", Set.of(0));
    }

    public static User createUserSellerE() {
        return new User("1", "Vinicius", "vinicius@email.com", "password", Set.of(0));
    }

    public static User createUserSeller() {
        return new User("1", "Cirilo", "cirilo@email.com", "password", Set.of(0));
    }

    public static Page<User> createPageUserSellers() {
        List<User> list = new ArrayList<>();
        User UserA = new User("1", "Cirilo", "Cirilo@email.com", "password", Set.of(0));
        User UserB = new User("2", "Maria Joaquina", "mariajoaquina@email.com", "password", Set.of(0));
        User UserC = new User("3", "Jaime", "Jaime@email.com", "password", Set.of(0));
        list.add(UserA);
        list.add(UserB);
        list.add(UserC);
        return new PageImpl<>(list);
    }

    public static UserRequestDTO createSellerUserRequestDto() {
        return UserRequestDTO.builder().name("Mario").email("mario@email.com").password("password").roles(Set.of(0)).build();
    }

    public static UserRequestDTO createSellerUserRequestDtoToThrow() {
        return UserRequestDTO.builder().name("Luigi").email("luigi@email.com").password("password").roles(Set.of(0)).build();
    }

    public static UserRequestDTO createSellerUserToUpdateRequestDto() {
        return UserRequestDTO.builder().name("Peach").email("peach@email.com").password("password").roles(Set.of(0)).build();
    }

    public static User createWarehouseManagerDefault(){
        return User.builder().name("teste").email("teste@teste.com").password("aaaa").roles(Set.of(2)).build();
    }

    public static User createWarehouseManagerDefault2(){
        return User.builder().name("teste").email("teste@teste2.com").password("aaaa").roles(Set.of(2)).build();
    }



}



