package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="carts")
public class Cart {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @OneToOne
    private User buyer;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartItem> items;

}
