package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "sellers")
@NoArgsConstructor
@Getter
@Setter
public class Seller extends User {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="roles_seller")
    private Set<Integer> roles = new HashSet<>();


    public Set<Role> getRoles() {
        return roles.stream().map(role -> Role.toEnum(role)).collect(Collectors.toSet());
    }

    public Seller(String id, String name, String email, String password, Set<Integer> roles) {
        super(id, name, email, password);
        this.roles = roles;
    }
}