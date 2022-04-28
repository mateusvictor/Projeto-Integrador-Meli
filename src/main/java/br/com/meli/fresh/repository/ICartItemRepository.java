package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartItemRepository extends JpaRepository<CartItem, String> {
}
