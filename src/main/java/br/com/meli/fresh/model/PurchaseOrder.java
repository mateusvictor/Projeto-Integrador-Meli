package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import br.com.meli.fresh.model.Buyer;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private LocalDateTime date;
    private OrderStatus orderStatus;

    @ManyToOne
    private Buyer buyer;

    @OneToMany
    private List<ProductOrder> products;

}
