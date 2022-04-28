package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartRepository extends JpaRepository<Cart, String> {
}
