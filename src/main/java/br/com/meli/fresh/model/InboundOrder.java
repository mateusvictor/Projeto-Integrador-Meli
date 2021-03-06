package br.com.meli.fresh.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inbound_orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    public Double calculateBatchesTotalVolume(){
        // Calculates the sum of all batches volumes
        Double batchesTotalVolume = 0.0;
        for (Batch batch : batchList){
            batchesTotalVolume += batch.getVolume();
        }
        return batchesTotalVolume;
    }
}