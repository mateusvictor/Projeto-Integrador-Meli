package br.com.meli.fresh.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String productType;
    private Float actualVolume;
    private Float maxVolume;

    @OneToMany(mappedBy = "section")
    private List<InboundOrder> inboundOrderList;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToOne
    private WarehouseManager warehouseManager;
}