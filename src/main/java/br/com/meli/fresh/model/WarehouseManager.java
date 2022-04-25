package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_managers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WarehouseManager extends User {
    @OneToOne
    private Warehouse warehouse;
}