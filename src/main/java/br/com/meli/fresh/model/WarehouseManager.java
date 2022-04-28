package br.com.meli.fresh.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_managers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WarehouseManager extends User {
    public WarehouseManager(String id, String name, String email, String password, Warehouse warehouse) {
        super.setEmail(email);
        super.setId(id);
        super.setName(name);
        super.setPassword(password);
        this.warehouse = warehouse;
    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;
}