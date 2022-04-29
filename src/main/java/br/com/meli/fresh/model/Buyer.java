package br.com.meli.fresh.model;

import lombok.Getter;
import lombok.Setter;

import lombok.*;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
public class Buyer extends User {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="roles_buyer")
    private Set<Integer> roles = new HashSet<>();


    public Set<Role> getRoles() {
        return roles.stream().map(role -> Role.toEnum(role)).collect(Collectors.toSet());
    }

    public Buyer(String id, String name, String email, String password, Set<Integer> roles) {
        super(id, name, email, password);
        this.roles = roles;
    }
}