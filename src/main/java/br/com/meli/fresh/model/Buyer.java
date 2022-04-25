package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "buyers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Buyer extends User {

}