package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="users")
@Builder
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="roles")
    private Set<Integer> roles = new HashSet<>();


    public Set<Role> getRoles() {
        return roles.stream().map(role -> Role.toEnum(role)).collect(Collectors.toSet());
    }
}
