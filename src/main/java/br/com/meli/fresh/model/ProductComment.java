package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="comments")
@Builder
public class ProductComment {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private User buyer;
    private LocalDateTime commentDateTime;
    private String description;
    private Integer rating;

}
