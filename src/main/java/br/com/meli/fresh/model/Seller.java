package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sellers")
@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class Seller extends User {

    public Seller(String id, String name, String email, String password) {
        super.setEmail(email);
        super.setId(id);
        super.setName(name);
        super.setPassword(password);
    }
}