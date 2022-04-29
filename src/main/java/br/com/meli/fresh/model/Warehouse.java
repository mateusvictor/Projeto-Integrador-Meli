package br.com.meli.fresh.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "warehouses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Warehouse {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String name;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<Section> sectionList;

    @OneToOne
    private User warehouseManager;
}