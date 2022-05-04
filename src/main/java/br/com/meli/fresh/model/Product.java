package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "is_active = true")
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
    private BigDecimal price;

    @ManyToOne
    private User seller;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Batch> batchList;

    @OneToMany(mappedBy = "product")
    private List<ProductComment> comments;

    private Boolean isActive = true;
}
