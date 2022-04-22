package br.com.meli.fresh.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_managers")
public class WarehouseManager extends User {
    @OneToOne
    private Warehouse warehouse;
}