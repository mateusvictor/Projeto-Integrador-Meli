package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "warehouse_managers")
@NoArgsConstructor
@Getter
@Setter
public class WarehouseManager extends User {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="roles_warehouse_manager")
    private Set<Integer> roles = new HashSet<>();


    public Set<Role> getRoles() {
        return roles.stream().map(role -> Role.toEnum(role)).collect(Collectors.toSet());
    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;
}