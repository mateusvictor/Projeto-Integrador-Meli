package br.com.meli.fresh.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inbound_orders")
public class InboundOrder {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private LocalDateTime orderDateTime;

    @OneToMany(mappedBy = "inboundOrder")
    private List<Batch> batchList;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;
}