package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "buyers")
@AllArgsConstructor
@Getter
@Setter
public class Buyer extends User {

}