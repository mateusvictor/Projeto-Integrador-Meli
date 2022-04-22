package br.com.meli.fresh.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String name;
    private String category;
    private Float minTemperature;
    private Float maxTemperature;
    private Float weight;

    @OneToMany(mappedBy = "product")
    private List<Batch> batchList;
}
